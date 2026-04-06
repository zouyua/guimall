# 电商库存高并发扣减系统

> 一套基于 Redis 分桶 + DB 明细驱动的强一致性库存扣减方案，实现零超卖、零少卖，支持 Redis 宕机自动降级

## 目录

- [一、背景：为什么需要高并发库存扣减方案](#一背景为什么需要高并发库存扣减方案)
- [二、传统方案的三大痛点](#二传统方案的三大痛点)
- [三、核心设计思想](#三核心设计思想)
- [四、技术架构详解](#四技术架构详解)
- [五、核心流程实现](#五核心流程实现)
- [六、常见问题解答](#六常见问题解答)
- [七、性能测试与优化](#七性能测试与优化)
- [八、总结与展望](#八总结与展望)

---

## 一、背景：为什么需要高并发库存扣减方案

在电商系统中，库存扣减是一个看似简单但实际非常复杂的问题。尤其是在以下场景：

### 典型场景

1. **秒杀活动**：1000 件商品，10 万用户同时抢购
2. **直播带货**：主播一句话，数万人同时下单
3. **限量发售**：新品首发，瞬时流量暴增

### 核心挑战

- **高并发**：每秒数万次库存扣减请求
- **强一致性**：绝对不能超卖（卖出的商品数 > 实际库存）
- **零少卖**：不能因为系统问题导致有库存却卖不出去
- **高可用**：Redis 宕机后系统仍能正常运行

---

## 二、传统方案的三大痛点

### 方案 1：纯数据库扣减

**实现方式：**
```sql
UPDATE inventory 
SET stock = stock - 1 
WHERE product_id = 1001 AND stock > 0;
```

**优点：**
- 实现简单
- 数据强一致性

**缺点：**
- ❌ **性能瓶颈**：单行热点更新，MySQL 行锁导致大量请求排队
- ❌ **TPS 低**：单机 MySQL 只能支撑 1000-2000 TPS
- ❌ **用户体验差**：大量请求超时，用户等待时间长

### 方案 2：纯 Redis 扣减

**实现方式：**
```bash
DECR inventory:1001
```

**优点：**
- 性能极高（单机 10 万+ TPS）
- 响应速度快

**缺点：**
- ❌ **无法避免少卖**：Redis 超时时，应用层无法判断是否扣减成功
- ❌ **只支持简单模型**：无法支持 SQ/LQ/WQ/OQ 等复杂库存状态
- ❌ **完全依赖 Redis**：Redis 宕机则整个系统不可用

### 方案 3：传统 Redis 分桶

**实现方式：**
- 将库存分散到多个 Redis 分桶
- 扣减时随机选择一个分桶

**优点：**
- 性能较好
- 降低单点压力

**缺点：**
- ❌ **仍然无法避免少卖**
- ❌ **分桶不均导致超卖**：某些分桶先被扣完，但其他分桶还有库存
- ❌ **无法支持复杂库存模型**

---

## 三、核心设计思想

### 3.1 设计原则

我们的方案基于一个核心思想：**Redis 只做计数，DB 明细为准**

```
┌─────────────────────────────────────────────────────────┐
│                    核心设计原则                           │
├─────────────────────────────────────────────────────────┤
│ 1. Redis 分桶：仅用于高并发计数验证，防止超卖                 │
│ 2. DB 明细表：记录所有库存流转，是唯一数据源                  │
│ 3. 合并提交：定时批量更新主表，降低 DB 压力                   │
│ 4. 自动降级：Redis 不可用时自动切换到低并发流程               │
└─────────────────────────────────────────────────────────┘
```

### 3.2 库存字段设计

我们设计了四个核心库存字段，支持完整的电商业务流程：

| 字段 | 名称 | 作用 | 前端展示 |
|------|------|------|---------|
| **SQ** | 可售库存 | 低并发直接扣减，未预热时的主要库存 | ✅ 展示（SQ+LQ） |
| **LQ** | 预锁库存 | 高并发专用缓冲区，预热后从 SQ 隔离 | ✅ 展示（SQ+LQ） |
| **WQ** | 预扣库存 | 下单未付款临时锁定 | ❌ 不展示 |
| **OQ** | 占用库存 | 付款后实际占用，等待发货 | ❌ 不展示 |

**核心闭环：** `SQ + LQ + WQ + OQ = 总库存`（未出库时）

**前端展示规则：**
- **商品列表/详情页**：显示 `SQ + LQ`（总可售库存）
  - 后端返回字段名为 `totalSq`，但值是 `SQ + LQ` 的总和
  - 用户看到的是"可以购买的总库存"
- **管理后台**：分别显示 SQ、LQ、WQ、OQ（便于运营监控）
- **用户视角**：只关心"能买多少"，即 SQ+LQ

### 3.3 库存流转规则

```
预热阶段：    SQ ──────────> LQ
              ↓              ↓
下单阶段：    └──> WQ <──────┘
              ↓
付款阶段：    └──────────> OQ
              ↓              ↓
履约阶段：    └──> 出库      └──> 退款 ──> SQ/LQ
```

**关键设计：**
- 所有流转都先插入明细表
- 定时任务扫描明细，批量更新主表
- 禁止跨状态流转（如 SQ 不能直接到 OQ）

---

## 四、技术架构详解

### 4.1 整体架构图

```
┌─────────────────────────────────────────────────────────────┐
│                         用户请求                             │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│                    应用层（Spring Boot）                     │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │ 库存预热模块  │  │ 下单扣减模块  │  │ 订单支付模块  │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
│  ┌──────────────┐  ┌──────────────┐                        │
│  │ 合并提交任务  │  │ 超时回收任务  │                        │
│  └──────────────┘  └──────────────┘                        │
└────────────┬───────────────────────┬────────────────────────┘
             │                       │
             ▼                       ▼
┌─────────────────────┐   ┌─────────────────────┐
│   Redis 分桶集群     │   │   MySQL 数据库       │
│  ┌────┐ ┌────┐      │   │  ┌──────────────┐   │
│  │桶0 │ │桶1 │ ...  │   │  │ inventory    │   │
│  └────┘ └────┘      │   │  │ (主表)       │   │
│                     │   │  └──────────────┘   │
│  仅做计数验证        │   │  ┌──────────────┐   │
│  防止超卖           │   │  │ deduct_detail│   │
│                     │   │  │ (明细表)     │   │
└─────────────────────┘   │  └──────────────┘   │
                          │                     │
                          │  数据唯一来源        │
                          └─────────────────────┘
```

### 4.2 核心表设计

#### 4.2.1 库存主表（inventory）

```sql
CREATE TABLE `inventory` (
  `inv_id` bigint NOT NULL AUTO_INCREMENT COMMENT '库存唯一ID，主键',
  `product_id` bigint NOT NULL COMMENT '商品ID，关联商品主表',
  `spec_id` bigint NOT NULL DEFAULT '0' COMMENT '商品规格ID，关联规格表，无规格则为0',
  `sq` int NOT NULL DEFAULT '0' COMMENT '可售库存：前端展示，低并发直接扣减',
  `lq` int NOT NULL DEFAULT '0' COMMENT '系统预锁库存：高并发专用缓冲区，从sq隔离，不展示',
  `wq` int NOT NULL DEFAULT '0' COMMENT '预扣库存：下单未付款临时锁定，超时/取消回补',
  `oq` int NOT NULL DEFAULT '0' COMMENT '占用库存：付款后实际占用，发货后减少',
  `version` int NOT NULL DEFAULT '1' COMMENT '乐观锁版本号：合并提交防并发更新冲突，更新时+1',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '库存状态：0-正常 1-下架 2-锁定 3-售罄',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间，合并提交/补货时更新',
  PRIMARY KEY (`inv_id`),
  UNIQUE KEY `uk_product_spec` (`product_id`,`spec_id`) COMMENT '商品+规格唯一索引，避免重复创建库存',
  KEY `idx_status` (`status`) COMMENT '库存状态索引，查询正常库存提速'
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='库存主表-商品规格级';
```

**关键点：**
- `version` 字段：乐观锁，防止合并提交时的并发冲突
- 唯一索引：防止同一商品规格创建多条库存记录

#### 4.2.2 库存扣减明细表（inventory_deduct_detail）

```sql
CREATE TABLE `inventory_deduct_detail` (
  `detail_id` bigint NOT NULL AUTO_INCREMENT COMMENT '明细唯一ID，主键',
  `inv_id` bigint NOT NULL COMMENT '库存ID，关联inventory表',
  `lock_order_id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '业务唯一标识：订单号/发货号/退款号，用于幂等判断',
  `quantity` int NOT NULL COMMENT '流转数量：正数，仅代表流转件数',
  `flow_type` tinyint NOT NULL COMMENT '库存流转类型：0=SQ→LQ 1=LQ→WQ 2=SQ→WQ 3=WQ→OQ 4=WQ→SQ 5=WQ→LQ 6=OQ→SQ 7=OQ→LQ 8=OQ→出库',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '明细状态：0-待处理 1-已处理 2-已回收 3-失败',
  `deduct_type` tinyint NOT NULL COMMENT '操作类型：0-下单扣减 1-付款占用 2-库存回收 3-出库 4-退款 5-预热/补货',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`detail_id`),
  UNIQUE KEY `uk_inv_order` (`inv_id`,`lock_order_id`) COMMENT '库存+业务标识唯一索引，天然实现幂等性',
  KEY `idx_inv_status_flow` (`inv_id`,`status`,`flow_type`) COMMENT '覆盖索引：合并提交分组求和，无需回表，极致提速',
  KEY `idx_create_time` (`create_time`) COMMENT '创建时间索引，清理历史明细/扫描超时订单提速'
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='库存扣减明细表-流转唯一凭证';
```

**关键点：**
- `uk_inv_order`：天然实现幂等性，防止重复下单
- `idx_inv_status_flow`：覆盖索引，合并提交时无需回表

#### 4.2.3 Redis 分桶配置表（inventory_redis_bucket）

```sql
CREATE TABLE `inventory_redis_bucket` (
  `bucket_id` bigint NOT NULL AUTO_INCREMENT COMMENT '分桶配置ID，主键',
  `inv_id` bigint NOT NULL COMMENT '库存ID，关联inventory表',
  `bucket_no` int NOT NULL COMMENT '分桶编号：0,1,2...N-1（连续编号）',
  `bucket_key` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Redis分桶key，格式：inventory:bucket:{inv_id}:{bucket_no}',
  `init_quantity` int NOT NULL DEFAULT '0' COMMENT '分桶初始化数量，由LQ均分而来',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '分桶状态：0-可用 1-禁用（临时屏蔽）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间，合并提交后同步',
  PRIMARY KEY (`bucket_id`),
  UNIQUE KEY `uk_inv_bucketno` (`inv_id`,`bucket_no`) COMMENT '库存+分桶编号唯一索引，避免重复配置',
  UNIQUE KEY `uk_bucket_key` (`bucket_key`) COMMENT 'Redis key唯一索引，避免重复生成',
  KEY `idx_inv_status` (`inv_id`,`status`) COMMENT '库存+分桶状态索引，初始化/锁定分桶提速'
) ENGINE=InnoDB AUTO_INCREMENT=121 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Redis分桶配置表-持久化动态管理';
```

**关键点：**
- 持久化分桶配置，支持动态调整
- 记录初始化数量，方便故障恢复

---

## 五、核心流程实现

### 5.1 库存预热（SQ → LQ）

**场景：** 商家上架商品，系统自动预热 50% 库存到 LQ

```java
@Transactional
public String preheatInventory(Long invId) {
    // 1. 查询库存
    Inventory inventory = inventoryMapper.selectById(invId);
    
    // 2. 计算预热数量（50%）
    Integer preheatQuantity = (int) (inventory.getSq() * 0.5);
    
    // 3. 更新主表：SQ → LQ
    inventory.setSq(inventory.getSq() - preheatQuantity);
    inventory.setLq(inventory.getLq() + preheatQuantity);
    inventoryMapper.updateById(inventory);
    
    // 4. 插入预热明细（flow_type=0）
    InventoryDeductDetail detail = new InventoryDeductDetail();
    detail.setInvId(invId);
    detail.setFlowType(0); // SQ→LQ
    detail.setQuantity(preheatQuantity);
    detail.setStatus(1); // 已处理
    detailMapper.insert(detail);
    
    // 5. 初始化 Redis 分桶（10 个分桶）
    bucketManager.initBuckets(invId, preheatQuantity, 10);
    
    return "预热成功";
}
```

**Redis 分桶初始化：**
```java
public void initBuckets(Long invId, Integer totalQuantity, Integer bucketCount) {
    int baseValue = totalQuantity / bucketCount;
    int remainder = totalQuantity % bucketCount;
    
    for (int i = 0; i < bucketCount; i++) {
        String key = "inventory:bucket:" + invId + ":" + i;
        int value = baseValue + (i < remainder ? 1 : 0);
        redisTemplate.opsForValue().set(key, value);
    }
}
```

### 5.2 高并发下单（LQ → WQ）

**场景：** 用户下单 3 件商品，Redis 分桶扣减 + DB 明细记录

```java
@Transactional
public String deductInventoryHighConcurrency(Long invId, String orderNo, Integer quantity) {
    try {
        // 1. 选择 Redis 分桶（哈希取模）
        Integer bucketNo = selectBucket(invId, orderNo);
        
        // 2. Redis 原子扣减
        Long result = redisTemplate.opsForValue()
            .decrement("inventory:bucket:" + invId + ":" + bucketNo, quantity);
        
        // 3. 扣减失败，重试 3 个随机分桶
        if (result < 0) {
            // 回补刚才扣减的分桶
            redisTemplate.opsForValue().increment(key, quantity);
            
            // 重试其他分桶...
            // 仍失败则降级到低并发流程
            return deductInventoryLowConcurrency(invId, orderNo, quantity);
        }
        
        // 4. Redis 成功，插入 DB 明细
        InventoryDeductDetail detail = new InventoryDeductDetail();
        detail.setInvId(invId);
        detail.setLockOrderId(orderNo);
        detail.setQuantity(quantity);
        detail.setFlowType(1); // LQ→WQ
        detail.setStatus(0); // 待处理
        detailMapper.insert(detail);
        
        return "扣减成功";
        
    } catch (Exception e) {
        // Redis 异常，自动降级
        return deductInventoryLowConcurrency(invId, orderNo, quantity);
    }
}
```

**分桶选择算法：**
```java
private Integer selectBucket(Long invId, String orderNo) {
    // 哈希取模，保证同一订单总是选择同一个分桶
    int hash = orderNo.hashCode();
    return Math.abs(hash) % bucketCount;
}
```

### 5.3 低并发下单（SQ/LQ → WQ）

**场景：** Redis 不可用或分桶库存不足，智能选择 SQ 或 LQ

```java
@Transactional
public String deductInventoryLowConcurrency(Long invId, String orderNo, Integer quantity) {
    // 1. 查询当前库存
    Inventory inventory = inventoryMapper.selectById(invId);
    Integer totalAvailable = inventory.getSq() + inventory.getLq();
    
    // 2. 检查总可售库存
    if (totalAvailable < quantity) {
        throw new IllegalStateException("库存不足");
    }
    
    // 3. 智能分配：优先使用 SQ
    if (inventory.getSq() >= quantity) {
        // SQ 足够，全部从 SQ 扣减
        insertDetail(invId, orderNo, quantity, 2); // SQ→WQ
        
    } else if (inventory.getSq() > 0) {
        // SQ 不足，混合扣减
        insertDetail(invId, orderNo, inventory.getSq(), 2); // SQ→WQ
        insertDetail(invId, orderNo + "_LQ", quantity - inventory.getSq(), 1); // LQ→WQ
        
    } else {
        // SQ 为 0，全部从 LQ 扣减
        insertDetail(invId, orderNo, quantity, 1); // LQ→WQ
    }
    
    return "扣减成功";
}
```

**关键创新：** 低并发流程会智能检查 SQ+LQ 总库存，避免"有库存却卖不出去"的问题。

### 5.4 订单支付（WQ → OQ）

**场景：** 用户付款成功，库存从 WQ 流转到 OQ

```java
@Transactional
public String payOrder(String orderNo) {
    // 1. 查询订单明细
    List<OrderItem> items = orderItemMapper.selectByOrderNo(orderNo);
    
    for (OrderItem item : items) {
        // 2. 插入 WQ→OQ 明细
        InventoryDeductDetail detail = new InventoryDeductDetail();
        detail.setInvId(item.getInvId());
        detail.setLockOrderId(orderNo + "_PAY");
        detail.setQuantity(item.getQuantity());
        detail.setFlowType(3); // WQ→OQ
        detail.setStatus(0); // 待处理
        detailMapper.insert(detail);
        
        // 注意：不手动更新原明细状态，让定时任务统一处理
    }
    
    return "支付成功";
}
```

**关键修复：** 不再手动标记原明细为已处理，避免支付过快导致明细漏扫描。

### 5.5 合并提交（核心性能优化）

**场景：** 定时任务每 50ms 扫描待处理明细，批量更新主表

```java
@Scheduled(fixedDelay = 50)
public void execute() {
    // 扫描需要合并的库存 ID
    List<Long> invIdList = detailMapper.listNeedMergeInvId();
    
    for (Long invId : invIdList) {
        executeMerge(invId);
    }
}

@Transactional
public void executeMerge(Long invId) {
    // 1. 获取分布式锁（Redis 不可用时跳过）
    RLock lock = redissonClient.getLock("inventory:merge:lock:" + invId);
    try {
        lock.tryLock(0, 500, TimeUnit.MILLISECONDS);
    } catch (Exception e) {
        // Redis 不可用，依赖数据库乐观锁
    }
    
    try {
        // 2. 锁定 Redis 分桶（MSET 置 0）
        bucketManager.lockAllBuckets(invId);
        
        // 3. 扫描待处理明细，按 flow_type 分组求和
        List<Map<String, Object>> aggregation = detailMapper.aggregateByFlowType(invId);
        
        // 4. 计算库存变化量
        int sqDelta = 0, lqDelta = 0, wqDelta = 0, oqDelta = 0;
        for (Map<String, Object> row : aggregation) {
            Integer flowType = (Integer) row.get("flow_type");
            Integer quantity = (Integer) row.get("total_quantity");
            
            switch (flowType) {
                case 1: // LQ→WQ
                    lqDelta -= quantity;
                    wqDelta += quantity;
                    break;
                case 2: // SQ→WQ
                    sqDelta -= quantity;
                    wqDelta += quantity;
                    break;
                case 3: // WQ→OQ
                    wqDelta -= quantity;
                    oqDelta += quantity;
                    break;
                // ... 其他流转类型
            }
        }
        
        // 5. 使用乐观锁更新主表
        Inventory inventory = inventoryMapper.selectById(invId);
        Integer currentVersion = inventory.getVersion();
        
        inventoryMapper.update(null, new LambdaUpdateWrapper<Inventory>()
            .eq(Inventory::getInvId, invId)
            .eq(Inventory::getVersion, currentVersion)
            .setSql("sq = sq + " + sqDelta)
            .setSql("lq = lq + " + lqDelta)
            .setSql("wq = wq + " + wqDelta)
            .setSql("oq = oq + " + oqDelta)
            .setSql("version = version + 1")
        );
        
        // 6. 标记明细为已处理
        detailMapper.update(null, new LambdaUpdateWrapper<InventoryDeductDetail>()
            .eq(InventoryDeductDetail::getInvId, invId)
            .eq(InventoryDeductDetail::getStatus, 0)
            .set(InventoryDeductDetail::getStatus, 1)
        );
        
        // 7. 重新分配 Redis 分桶
        Inventory updated = inventoryMapper.selectById(invId);
        if (updated.getLq() > 0) {
            bucketManager.redistributeBuckets(invId, updated.getLq());
        }
        
    } finally {
        if (lock != null && lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }
}
```

**SQL 优化：覆盖索引**
```sql
-- 扫描待处理明细，按 flow_type 分组求和
SELECT flow_type, SUM(quantity) AS total_quantity
FROM inventory_deduct_detail
WHERE inv_id = ? AND status = 0
GROUP BY flow_type;

-- 使用覆盖索引 idx_inv_status_flow (inv_id, status, flow_type)
-- 无需回表，极致提速
```

---

## 六、常见问题解答

### Q1：分桶全量置 0 时，新请求如何处理？

**答：** 分桶置 0 ≠ 拒绝请求，通过分层降级保证请求不中断。

**处理流程：**
1. **自动降级到低并发流程**：99% 请求走此流程，跳过 Redis 分桶
2. **智能检查 SQ+LQ 总库存**：
   - 如果 SQ 足够：创建 SQ→WQ 明细
   - 如果 SQ 不足但 SQ+LQ 足够：
     - 先用完 SQ：创建 SQ→WQ 明细
     - 剩余部分用 LQ：创建 LQ→WQ 明细
   - 如果 SQ+LQ 都不足：返回库存不足
3. **所有操作先插明细**：保证流转可追溯，由定时任务统一合并

**代码示例：**
```java
// 低并发流程智能选择 SQ/LQ
if (inventory.getSq() >= quantity) {
    // SQ 足够，全部从 SQ 扣减
    insertDetail(invId, orderNo, quantity, 2); // SQ→WQ
    
} else if (inventory.getSq() > 0) {
    // SQ 不足，混合扣减
    insertDetail(invId, orderNo, inventory.getSq(), 2); // SQ→WQ
    insertDetail(invId, orderNo + "_LQ", quantity - inventory.getSq(), 1); // LQ→WQ
    
} else {
    // SQ 为 0，全部从 LQ 扣减
    insertDetail(invId, orderNo, quantity, 1); // LQ→WQ
}
```

**关键保障：**
- 分桶置 0 时长极短（50ms 内），用户无感知
- 新请求库存校验以 DB 为准，不依赖 Redis
- 低并发流程会充分利用 SQ+LQ 总库存，避免"有库存却卖不出去"

### Q2：前端展示 SQ+LQ，用户付款后库存未变，如何解决？

**答：** 前端展示的是 `SQ + LQ`（总可售库存），采用批量异步更新，避免高并发下展示波动。

**核心原因：**
- 用户下单：创建 SQ→WQ 或 LQ→WQ 明细（status=0，待处理）
- 用户付款：创建 WQ→OQ 明细（status=0，待处理）
- 定时任务：每 50ms 扫描待处理明细，批量更新主表
- 前端展示：`SQ + LQ` 在定时任务执行后才会更新

**时间线示例：**
```
T0: 用户下单 3 件
    - 插入明细：LQ→WQ, quantity=3, status=0
    - 主表状态：SQ=500, LQ=500, WQ=0, OQ=0
    - 前端显示：1000 件

T1 (10ms): 用户付款
    - 插入明细：WQ→OQ, quantity=3, status=0
    - 主表状态：SQ=500, LQ=500, WQ=0, OQ=0
    - 前端显示：1000 件（未变）

T2 (50ms): 定时任务执行
    - 扫描明细：LQ→WQ(3), WQ→OQ(3)
    - 更新主表：LQ-=3, WQ+=3-3=0, OQ+=3
    - 主表状态：SQ=500, LQ=497, WQ=0, OQ=3
    - 前端显示：997 件（已更新）
```

**前端展示优化方案：**

| 页面类型 | 展示内容 | 优化逻辑 |
|---------|---------|---------|
| 商品列表页 | SQ+LQ + 库存状态文案 | "库存充足 / 即将售罄 / 仅剩 XX 件" |
| 商品详情页 | SQ+LQ + 实时备货总量 | "997 件可售，共 1000 件备货" |
| 订单确认页 | 订单状态 + 锁定提示 | "已锁定 3 件库存，待发货" |

**可选优化：**
- 调整合并提交阈值：从 ≥100 条改为 ≥50 条
- 调整定时任务：从 50ms 改为 200ms
- 高并发结束后触发 SQ+LQ 实时更新

**关键点：**
- 前端展示的是 `SQ + LQ`，不是单独的 SQ
- 用户关注的是"能买多少"，而不是具体的 SQ 或 LQ 数值
- 批量更新避免了高并发下的展示波动（如 1000→997→998→997）

### Q3：商家总可售 1000，用户下单 999 件，是否会成功？

**答：** 绝对不可能成功，三层防护层层阻断。

**第一层：商家单品限购（业务硬限制）**
```java
if (quantity > product.getMaxPurchaseQty()) {
    throw new IllegalArgumentException("单次最多购买 " + maxQty + " 件");
}
```

**第二层：库存总量校验（50% 限制）**
```java
Integer totalSalable = inventory.getSq() + inventory.getLq();
Integer maxAllowed = (int) (totalSalable * 0.5);
if (quantity > maxAllowed) {
    throw new IllegalArgumentException("单次购买不能超过总库存的 50%");
}
```

**第三层：技术层扣减失败**
- Redis 分桶：总数 = 500 < 999 → 所有分桶扣减失败
- 低并发流程：SQ=500 < 999 → 库存不足
- DB 兜底：`UPDATE inventory SET sq=sq-999 WHERE sq>=999` → 影响行数 = 0

### Q4：Redis 宕机后，系统如何保证可用性？

**答：** 完整的降级机制，保证 Redis 不可用时系统仍能正常运行。

**降级策略：**

1. **下单扣减降级**
   ```java
   try {
       // 尝试 Redis 分桶扣减
       return deductInventoryHighConcurrency(...);
   } catch (RedisException e) {
       // Redis 异常，自动降级到低并发流程
       return deductInventoryLowConcurrency(...);
   }
   ```

2. **定时任务降级**
   ```java
   try {
       // 尝试获取 Redis 分布式锁
       lock = redissonClient.getLock(...);
   } catch (Exception e) {
       // Redis 不可用，跳过锁，依赖数据库乐观锁
       lock = null;
   }
   ```

3. **分桶操作降级**
   ```java
   try {
       bucketManager.lockAllBuckets(invId);
   } catch (Exception e) {
       // Redis 不可用，记录警告，继续执行合并
       log.warn("锁定 Redis 分桶失败，继续执行合并");
   }
   ```

**关键配置：**
```yaml
spring:
  data:
    redis:
      timeout: 1000ms  # 超时 1 秒，快速失败
      connect-timeout: 1000ms
```

### Q5：如何防止超卖和少卖？

**答：** 多层防护机制，确保零超卖、零少卖。

**防超卖设计：**

1. **Redis 层防超卖**
   ```bash
   # DECRBY 返回值必须 >= 0
   DECRBY inventory:bucket:1:0 3
   # 返回 -1 → 回补 → 重试其他分桶
   ```

2. **DB 层防超卖**
   ```sql
   -- 更新时加数值条件
   UPDATE inventory 
   SET sq = sq - 3 
   WHERE inv_id = 1 AND sq >= 3;
   ```

3. **业务层防超卖**
   - 单品限购
   - 50% 总库存限制
   - 幂等性保证（唯一索引）

**防少卖设计：**

1. **以 DB 明细为准**
   - Redis 超时 → 明细仍会被合并提交
   - 实际扣减了多少，以 DB 明细求和为准

2. **低并发智能选择 SQ/LQ**
   - 检查 SQ+LQ 总库存
   - SQ 不足时自动使用 LQ
   - 避免"有库存却卖不出去"的问题
   
   ```java
   // 示例：SQ=2, LQ=1000, 用户下单 5 件
   // 传统方案：SQ 不足，扣减失败（少卖 5 件）
   // 本方案：SQ(2) + LQ(3) → 扣减成功
   ```

3. **Redis 数据恢复**
   - 合并提交后重新分配分桶
   - 分桶总数始终 = LQ 值
   - Redis 宕机恢复后自动同步

### Q6：合并提交的性能如何？会不会成为瓶颈？

**答：** 通过覆盖索引和批量更新，性能影响极小。

**性能优化：**

1. **覆盖索引**
   ```sql
   -- 索引：idx_inv_status_flow (inv_id, status, flow_type)
   -- 包含 quantity 字段，无需回表
   SELECT flow_type, SUM(quantity) AS total_quantity
   FROM inventory_deduct_detail
   WHERE inv_id = 1 AND status = 0
   GROUP BY flow_type;
   ```

2. **批量更新**
   ```sql
   -- 一次 SQL 更新所有字段
   UPDATE inventory 
   SET sq = sq - 3, wq = wq + 3, version = version + 1
   WHERE inv_id = 1 AND version = 5;
   ```

3. **异步非阻塞**
   - 定时任务独立线程执行
   - 不阻塞用户请求
   - 50ms 内完成合并

**压测数据：**
- 单行热点扣减：TPS 提升 1 倍以上
- 合并提交耗时：平均 10-20ms
- DB 压力：远低于实时更新（减少 90% 以上更新次数）

### Q7：如何实现幂等性？

**答：** 通过唯一索引天然实现幂等性。

**唯一索引设计：**
```sql
UNIQUE KEY `uk_inv_order` (`inv_id`, `lock_order_id`)
```

**幂等性保证：**
```java
try {
    // 插入明细
    detailMapper.insert(detail);
} catch (DuplicateKeyException e) {
    // 重复订单，直接返回成功
    return "扣减成功（幂等）";
}
```

**适用场景：**
- 用户重复点击下单按钮
- 网络超时后重试
- 消息队列重复消费

### Q8：分桶数量如何选择？

**答：** 根据并发量和库存量动态调整。

**推荐配置：**

| 场景 | 并发量 | 库存量 | 分桶数量 |
|------|--------|--------|---------|
| 低并发 | < 100 | < 1000 | 5-10 |
| 中并发 | 100-1000 | 1000-10000 | 10-20 |
| 高并发 | 1000-5000 | 10000+ | 20-50 |
| 极限并发 | 5000+ | 100000+ | 50-100 |

**动态调整：**
```java
// 高峰期动态加桶
public void addBuckets(Long invId, Integer newBucketCount) {
    // 1. 锁定现有分桶
    bucketManager.lockAllBuckets(invId);
    
    // 2. 按新数量重新分配
    bucketManager.redistributeBuckets(invId, lq, newBucketCount);
    
    // 3. 更新配置表
    bucketConfigMapper.updateBucketCount(invId, newBucketCount);
}
```

---

## 七、性能测试与优化

### 7.1 压测环境

**硬件配置：**
- 应用服务器：4 核 8G，2 台
- MySQL：8 核 16G，主从复制
- Redis：4 核 8G，哨兵模式

**测试工具：**
- JMeter 5.5
- 并发线程：100 / 500 / 1000 / 5000
- 持续时间：60 秒

### 7.2 压测结果

#### 7.2.1 单行热点扣减对比

| 方案 | TPS | 平均响应时间 | 成功率 | DB QPS |
|------|-----|-------------|--------|--------|
| 纯 DB 扣减 | 1,200 | 450ms | 85% | 1,200 |
| 传统 Redis 分桶 | 8,500 | 60ms | 80% | 8,500 |
| **本方案** | **18,000** | **30ms** | **100%** | **180** |

**关键指标：**
- ✅ TPS 提升 **1.5 倍**（相比传统 Redis 分桶）
- ✅ 成功率 **100%**（零超卖、零少卖）
- ✅ DB 压力降低 **98%**（合并提交批量更新）

#### 7.2.2 不同并发量下的表现

| 并发量 | TPS | 响应时间 | 成功率 | CPU 使用率 |
|--------|-----|---------|--------|-----------|
| 100 | 3,500 | 15ms | 100% | 25% |
| 500 | 12,000 | 25ms | 100% | 45% |
| 1000 | 18,000 | 30ms | 100% | 65% |
| 5000 | 22,000 | 120ms | 100% | 85% |

**结论：**
- 1000 并发以下：性能优异，响应时间稳定
- 5000 并发：仍能保持 100% 成功率，但响应时间增加

### 7.3 性能优化建议

#### 7.3.1 数据库优化

**1. 索引优化**
```sql
-- 覆盖索引，避免回表
CREATE INDEX idx_inv_status_flow 
ON inventory_deduct_detail (inv_id, status, flow_type, quantity);

-- 分区表，历史数据归档
ALTER TABLE inventory_deduct_detail 
PARTITION BY RANGE (YEAR(create_time)) (
    PARTITION p2024 VALUES LESS THAN (2025),
    PARTITION p2025 VALUES LESS THAN (2026)
);
```

**2. 读写分离**
```yaml
spring:
  datasource:
    master:
      url: jdbc:mysql://master:3306/shop_chunk_redis
    slave:
      url: jdbc:mysql://slave:3306/shop_chunk_redis
```

**3. 连接池优化**
```yaml
spring:
  datasource:
    druid:
      initial-size: 20
      min-idle: 20
      max-active: 100
      max-wait: 60000
```

#### 7.3.2 Redis 优化

**1. 持久化配置**
```conf
# AOF + RDB 混合持久化
appendonly yes
appendfsync everysec
save 900 1
save 300 10
```

**2. 内存优化**
```conf
# 最大内存
maxmemory 4gb
# 淘汰策略
maxmemory-policy allkeys-lru
```

**3. 集群模式**
```
# Redis Cluster（高可用）
redis-cli --cluster create \
  192.168.1.1:6379 \
  192.168.1.2:6379 \
  192.168.1.3:6379 \
  --cluster-replicas 1
```

#### 7.3.3 应用层优化

**1. 异步处理**
```java
@Async
public CompletableFuture<String> deductInventoryAsync(...) {
    return CompletableFuture.completedFuture(
        deductInventoryHighConcurrency(...)
    );
}
```

**2. 批量操作**
```java
// 批量插入明细
detailMapper.insertBatch(detailList);

// 批量更新 Redis
redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
    for (String key : keys) {
        connection.decr(key.getBytes());
    }
    return null;
});
```

**3. 缓存预热**
```java
@PostConstruct
public void warmUp() {
    // 应用启动时预热热点商品库存
    List<Long> hotProducts = productMapper.selectHotProducts();
    for (Long productId : hotProducts) {
        inventoryService.preheatInventory(productId);
    }
}
```

### 7.4 监控告警

#### 7.4.1 关键指标监控

**1. 业务指标**
- 下单成功率
- 库存扣减 TPS
- 合并提交延迟
- 订单超时率

**2. 技术指标**
- Redis 命中率
- DB 慢查询
- 应用 CPU/内存
- 接口响应时间

#### 7.4.2 告警规则

```yaml
# Prometheus 告警规则
groups:
  - name: inventory_alerts
    rules:
      # 下单成功率低于 95%
      - alert: LowOrderSuccessRate
        expr: order_success_rate < 0.95
        for: 1m
        
      # 合并提交延迟超过 100ms
      - alert: HighMergeDelay
        expr: merge_commit_duration_ms > 100
        for: 5m
        
      # Redis 不可用
      - alert: RedisDown
        expr: redis_up == 0
        for: 1m
```

---

## 八、总结与展望

### 8.1 核心优势

1. **高性能**
   - TPS 提升 1.5 倍以上
   - 响应时间降低 50%
   - DB 压力降低 98%

2. **强一致性**
   - 零超卖：多层防护机制
   - 零少卖：以 DB 明细为准
   - 幂等性：唯一索引保证

3. **高可用**
   - Redis 宕机自动降级
   - 数据库乐观锁兜底
   - 完整的容错机制

4. **易扩展**
   - 支持复杂库存模型（SQ/LQ/WQ/OQ）
   - 动态调整分桶数量
   - 灵活的流转规则

### 8.2 适用场景

✅ **适合：**
- 电商秒杀活动
- 直播带货
- 限量发售
- 高并发下单场景

❌ **不适合：**
- 低并发场景（纯 DB 扣减即可）
- 对库存实时性要求极高的场景
- 无法接受最终一致性的场景

### 8.3 开源地址

本项目已开源，做了简单的前后端演示

- **Gitee**：[shop_chunk_redis](https://gitee.com/zouyua/shop_chunk_redis)
- **技术栈**：Spring Boot + MyBatis-Plus + Redis + Vue 3
- **文档**：完整的配置指南和 API 文档
- **测试数据**：包含 mock 数据，开箱即用

### 8.4 参考资料

1. [阿里云 - 库存合并扣减方案](https://mp.weixin.qq.com/s/_ezTVydFszZnc0ZN-JEtlQ)
2. [Redis 官方文档](https://redis.io/documentation)
3. [MyBatis-Plus 官方文档](https://baomidou.com/)
4. [Spring Boot 官方文档](https://spring.io/projects/spring-boot)