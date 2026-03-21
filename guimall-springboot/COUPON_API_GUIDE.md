# 优惠券功能 API 使用指南

## 功能概述

前端用户层优惠券功能已完成，包括以下核心功能：
- 查看可领取的优惠券列表
- 领取优惠券
- 查看我的优惠券
- 订单使用优惠券
- 查询订单可用优惠券

## API 接口列表

### 1. 获取可领取的优惠券列表

**接口地址**: `GET /coupon/available/list`

**请求参数**:
```
memberId: Long (必填) - 会员ID
```

**请求示例**:
```
GET /coupon/available/list?memberId=1001
```

**响应示例**:
```json
{
  "success": true,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "type": 0,
      "name": "新人专享券",
      "platform": 0,
      "amount": 10.00,
      "perLimit": 1,
      "minPoint": 50.00,
      "startTime": "2026-03-01T00:00:00",
      "endTime": "2026-12-31T23:59:59",
      "useType": 0,
      "note": "全场通用",
      "publishCount": 1000,
      "useCount": 50,
      "receiveCount": 200,
      "enableTime": "2026-03-01T00:00:00",
      "memberLevel": null,
      "received": false,
      "remainCount": 800
    }
  ]
}
```

**字段说明**:
- `received`: 当前会员是否已领取
- `remainCount`: 剩余可领取数量
- `type`: 0全场赠券 1会员赠券 2购物赠券 3注册赠券
- `useType`: 0全场 1指定分类 2指定商品

---

### 2. 领取优惠券

**接口地址**: `POST /coupon/receive`

**请求体**:
```json
{
  "couponId": 1,
  "memberId": 1001,
  "memberNickname": "张三"
}
```

**响应示例**:
```json
{
  "success": true,
  "message": "领取成功",
  "data": null
}
```

**错误场景**:
- 优惠券不存在: `{"success": false, "message": "优惠券不存在"}`
- 领取未开始: `{"success": false, "message": "优惠券领取未开始"}`
- 优惠券已过期: `{"success": false, "message": "优惠券已过期"}`
- 优惠券已领完: `{"success": false, "message": "优惠券已领完"}`
- 已达到领取上限: `{"success": false, "message": "已达到领取上限"}`

---

### 3. 获取会员的优惠券列表

**接口地址**: `GET /coupon/member/list`

**请求参数**:
```
memberId: Long (必填) - 会员ID
useStatus: Integer (可选) - 使用状态：0未使用 1已使用 2已过期，不传查询全部
```

**请求示例**:
```
GET /coupon/member/list?memberId=1001&useStatus=0
```

**响应示例**:
```json
{
  "success": true,
  "message": "操作成功",
  "data": [
    {
      "historyId": 101,
      "couponId": 1,
      "couponName": "新人专享券",
      "couponCode": "CPN1711012345678ABCD1234",
      "amount": 10.00,
      "minPoint": 50.00,
      "startTime": "2026-03-01T00:00:00",
      "endTime": "2026-12-31T23:59:59",
      "useType": 0,
      "useStatus": 0,
      "receiveTime": "2026-03-21T10:30:00",
      "useTime": null,
      "orderId": null,
      "orderSn": null
    }
  ]
}
```

**字段说明**:
- `historyId`: 优惠券领取记录ID（使用优惠券时需要）
- `useStatus`: 0未使用 1已使用 2已过期

---

### 4. 使用优惠券

**接口地址**: `POST /coupon/use`

**请求体**:
```json
{
  "historyId": 101,
  "orderId": 2001,
  "orderSn": "202603211030001",
  "memberId": 1001
}
```

**响应示例**:
```json
{
  "success": true,
  "message": "使用成功",
  "data": null
}
```

**错误场景**:
- 优惠券领取记录不存在: `{"success": false, "message": "优惠券领取记录不存在"}`
- 优惠券不属于当前会员: `{"success": false, "message": "该优惠券不属于当前会员"}`
- 优惠券已使用: `{"success": false, "message": "优惠券已使用或已过期"}`
- 优惠券已过期: `{"success": false, "message": "优惠券已过期"}`

---

### 5. 获取订单可用的优惠券列表

**接口地址**: `GET /coupon/order/available/list`

**请求参数**:
```
memberId: Long (必填) - 会员ID
totalAmount: BigDecimal (必填) - 订单总金额
```

**请求示例**:
```
GET /coupon/order/available/list?memberId=1001&totalAmount=100.00
```

**响应示例**:
```json
{
  "success": true,
  "message": "操作成功",
  "data": [
    {
      "historyId": 101,
      "couponId": 1,
      "couponName": "新人专享券",
      "couponCode": "CPN1711012345678ABCD1234",
      "amount": 10.00,
      "minPoint": 50.00,
      "startTime": "2026-03-01T00:00:00",
      "endTime": "2026-12-31T23:59:59",
      "useType": 0,
      "useStatus": 0,
      "receiveTime": "2026-03-21T10:30:00"
    }
  ]
}
```

**说明**:
- 只返回满足订单金额要求的优惠券（订单金额 >= 最低消费金额）
- 自动过滤已过期的优惠券
- 只返回未使用的优惠券

---

## 业务流程

### 用户领取优惠券流程

```
1. 用户访问优惠券中心
   ↓
2. 调用 GET /coupon/available/list 获取可领取的优惠券列表
   ↓
3. 用户点击"领取"按钮
   ↓
4. 调用 POST /coupon/receive 领取优惠券
   ↓
5. 领取成功，优惠券进入"我的优惠券"
```

### 订单使用优惠券流程

```
1. 用户进入订单确认页面
   ↓
2. 调用 GET /coupon/order/available/list 获取可用优惠券
   ↓
3. 用户选择要使用的优惠券
   ↓
4. 提交订单时，调用 POST /coupon/use 使用优惠券
   ↓
5. 订单金额扣减优惠券金额
```

### 查看我的优惠券流程

```
1. 用户进入"我的优惠券"页面
   ↓
2. 调用 GET /coupon/member/list 获取优惠券列表
   - useStatus=0: 查看未使用的优惠券
   - useStatus=1: 查看已使用的优惠券
   - useStatus=2: 查看已过期的优惠券
   - 不传: 查看全部优惠券
```

---

## 数据字典

### 优惠券类型 (type)
- `0`: 全场赠券
- `1`: 会员赠券
- `2`: 购物赠券
- `3`: 注册赠券

### 使用平台 (platform)
- `0`: 全部
- `1`: 移动端
- `2`: WEB

### 使用范围 (useType)
- `0`: 全场
- `1`: 指定分类
- `2`: 指定商品

### 使用状态 (useStatus)
- `0`: 未使用
- `1`: 已使用
- `2`: 已过期

### 获取方式 (getType)
- `0`: 后台赠送
- `1`: 用户领取

---

## 注意事项

### 1. 优惠券领取限制
- 每个优惠券有 `perLimit` 字段，限制每人可领取的数量
- 优惠券有总数量限制 `publishCount`
- 优惠券有领取时间限制 `enableTime` 和有效期 `startTime` ~ `endTime`

### 2. 优惠券使用限制
- 订单金额必须 >= 优惠券的 `minPoint`（最低消费金额）
- 优惠券必须在有效期内
- 每张优惠券只能使用一次

### 3. 优惠券码生成规则
- 格式: `CPN + 时间戳 + 8位随机字符`
- 示例: `CPN1711012345678ABCD1234`
- 每次领取都会生成唯一的优惠券码

### 4. 并发控制
- 领取优惠券时使用了事务控制
- 建议在高并发场景下增加分布式锁或乐观锁

### 5. 过期优惠券处理
- 查询订单可用优惠券时，会自动将过期的优惠券状态更新为"已过期"
- 使用优惠券时，会检查是否过期

---

## 测试用例

### 正常场景测试

#### 1. 领取优惠券
```json
POST /coupon/receive
{
  "couponId": 1,
  "memberId": 1001,
  "memberNickname": "测试用户"
}
```

#### 2. 查看我的优惠券
```
GET /coupon/member/list?memberId=1001&useStatus=0
```

#### 3. 订单使用优惠券
```json
POST /coupon/use
{
  "historyId": 101,
  "orderId": 2001,
  "orderSn": "202603211030001",
  "memberId": 1001
}
```

### 异常场景测试

#### 1. 领取不存在的优惠券
```json
POST /coupon/receive
{
  "couponId": 99999,
  "memberId": 1001,
  "memberNickname": "测试用户"
}
```
预期: `{"success": false, "message": "优惠券不存在"}`

#### 2. 重复领取优惠券（超过限制）
```json
POST /coupon/receive
{
  "couponId": 1,
  "memberId": 1001,
  "memberNickname": "测试用户"
}
```
预期: `{"success": false, "message": "已达到领取上限"}`

#### 3. 使用别人的优惠券
```json
POST /coupon/use
{
  "historyId": 101,
  "orderId": 2001,
  "orderSn": "202603211030001",
  "memberId": 9999
}
```
预期: `{"success": false, "message": "该优惠券不属于当前会员"}`

#### 4. 订单金额不满足最低消费
```
GET /coupon/order/available/list?memberId=1001&totalAmount=10.00
```
预期: 返回空列表或不包含最低消费金额大于10的优惠券

---

## 集成建议

### 前端集成

#### 1. 优惠券中心页面
```javascript
// 获取可领取的优惠券列表
async function getAvailableCoupons(memberId) {
  const response = await axios.get('/coupon/available/list', {
    params: { memberId }
  });
  return response.data;
}

// 领取优惠券
async function receiveCoupon(couponId, memberId, memberNickname) {
  const response = await axios.post('/coupon/receive', {
    couponId,
    memberId,
    memberNickname
  });
  return response.data;
}
```

#### 2. 我的优惠券页面
```javascript
// 获取我的优惠券列表
async function getMyCoupons(memberId, useStatus) {
  const response = await axios.get('/coupon/member/list', {
    params: { memberId, useStatus }
  });
  return response.data;
}
```

#### 3. 订单确认页面
```javascript
// 获取订单可用优惠券
async function getOrderAvailableCoupons(memberId, totalAmount) {
  const response = await axios.get('/coupon/order/available/list', {
    params: { memberId, totalAmount }
  });
  return response.data;
}

// 使用优惠券
async function useCoupon(historyId, orderId, orderSn, memberId) {
  const response = await axios.post('/coupon/use', {
    historyId,
    orderId,
    orderSn,
    memberId
  });
  return response.data;
}
```

### 订单模块集成

在提交订单时，如果用户选择了优惠券：

```java
// 1. 验证优惠券是否可用
Response couponResponse = couponService.findOrderAvailableCouponList(memberId, totalAmount);

// 2. 计算优惠后的金额
BigDecimal discountAmount = coupon.getAmount();
BigDecimal finalAmount = totalAmount.subtract(discountAmount);

// 3. 创建订单
Order order = createOrder(finalAmount);

// 4. 使用优惠券
UseCouponReqVO useCouponReq = new UseCouponReqVO();
useCouponReq.setHistoryId(historyId);
useCouponReq.setOrderId(order.getId());
useCouponReq.setOrderSn(order.getOrderSn());
useCouponReq.setMemberId(memberId);
couponService.useCoupon(useCouponReq);
```

---

## 后续优化建议

1. **分布式锁**: 在高并发场景下，领取优惠券时增加分布式锁
2. **缓存优化**: 将热门优惠券信息缓存到Redis
3. **异步处理**: 优惠券使用记录可以异步写入
4. **优惠券推荐**: 根据用户购物车商品推荐合适的优惠券
5. **优惠券组合**: 支持多张优惠券叠加使用
6. **优惠券分享**: 支持用户分享优惠券给好友
7. **定时任务**: 定时清理过期优惠券，更新优惠券状态

---

## 文件清单

### Controller
- `guimall-web/src/main/java/com/gg/guimall/web/controller/SmsCouponController.java`

### Service
- `guimall-web/src/main/java/com/gg/guimall/web/service/SmsCouponService.java`
- `guimall-web/src/main/java/com/gg/guimall/web/service/impl/SmsCouponServiceImpl.java`

### VO类
- `guimall-web/src/main/java/com/gg/guimall/web/model/vo/coupon/ReceiveCouponReqVO.java`
- `guimall-web/src/main/java/com/gg/guimall/web/model/vo/coupon/FindAvailableCouponListReqVO.java`
- `guimall-web/src/main/java/com/gg/guimall/web/model/vo/coupon/FindMemberCouponListRspVO.java`
- `guimall-web/src/main/java/com/gg/guimall/web/model/vo/coupon/FindCouponListRspVO.java`
- `guimall-web/src/main/java/com/gg/guimall/web/model/vo/coupon/UseCouponReqVO.java`

### 错误码
- `guimall-module-common/src/main/java/com/gg/guimall/common/enums/ResponseCodeEnum.java` (已添加优惠券相关错误码)
