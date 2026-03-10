# 商品管理API快速参考

## 📋 API端点总览

| 方法 | 端点 | 功能 | 必填字段 |
|------|------|------|---------|
| POST | `/admin/product/create` | 创建商品 | productCategoryId, farmerId, name, productSn, price, stock |
| POST | `/admin/product/list` | 分页查询 | current, size |
| GET | `/admin/product/{id}` | 查询详情 | id |
| POST | `/admin/product/update` | 修改商品 | id, productCategoryId, farmerId, name, productSn, price, stock |
| DELETE | `/admin/product/{id}` | 删除商品 | id |

---

## 🔑 必填字段速查表

### 创建商品 (POST /admin/product/create)
```
✓ productCategoryId (Long)    - 商品分类ID
✓ farmerId (Long)             - 农户ID
✓ name (String)               - 商品名称
✓ productSn (String)          - 商品货号
✓ price (BigDecimal)          - 销售价格
✓ stock (Integer)             - 库存
```

### 修改商品 (POST /admin/product/update)
```
✓ id (Long)                   - 商品ID
✓ productCategoryId (Long)    - 商品分类ID
✓ farmerId (Long)             - 农户ID
✓ name (String)               - 商品名称
✓ productSn (String)          - 商品货号
✓ price (BigDecimal)          - 销售价格
✓ stock (Integer)             - 库存
```

### 分页查询 (POST /admin/product/list)
```
✓ current (Long)              - 当前页码
✓ size (Long)                 - 每页大小
○ name (String)               - 商品名称（可选）
○ categoryId (Long)           - 分类ID（可选）
○ publishStatus (Integer)     - 上架状态（可选）
```

---

## 🧪 快速测试命令

### 1. 创建商品
```bash
curl -X POST http://localhost:8080/admin/product/create \
  -H "Content-Type: application/json" \
  -d '{
    "productCategoryId": 1,
    "farmerId": 1,
    "name": "新鲜苹果",
    "productSn": "APPLE-001",
    "price": 9.99,
    "stock": 100
  }'
```

### 2. 分页查询
```bash
curl -X POST http://localhost:8080/admin/product/list \
  -H "Content-Type: application/json" \
  -d '{
    "current": 1,
    "size": 10
  }'
```

### 3. 查询详情
```bash
curl -X GET http://localhost:8080/admin/product/1
```

### 4. 修改商品
```bash
curl -X POST http://localhost:8080/admin/product/update \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "productCategoryId": 1,
    "farmerId": 1,
    "name": "新鲜苹果-已修改",
    "productSn": "APPLE-001",
    "price": 10.99,
    "stock": 150
  }'
```

### 5. 删除商品
```bash
curl -X DELETE http://localhost:8080/admin/product/1
```

---

## ⚠️ 异常状态码

| 状态码 | 含义 | 解决方案 |
|--------|------|---------|
| 10001 | 参数错误 | 检查必填字段是否都提供了 |
| 30001 | 商品不存在 | 检查商品ID是否正确 |
| 30002 | 商品分类不存在 | 检查分类ID是否有效 |
| 30003 | 商品创建失败 | 检查数据库连接和字段值 |
| 30004 | 商品修改失败 | 检查数据库连接和字段值 |
| 30005 | 商品删除失败 | 检查数据库连接 |
| 30006 | 商品数据无效 | ID不能为0或负数 |

---

## 📝 字段验证规则

### 字符串字段 (@NotBlank)
- `name` - 商品名称：不能为空或仅空格
- `productSn` - 商品货号：不能为空或仅空格

### 数值字段 (@NotNull)
- `productCategoryId` - 商品分类ID：不能为null
- `farmerId` - 农户ID：不能为null
- `price` - 销售价格：不能为null
- `stock` - 库存：不能为null
- `id` - 商品ID（修改时）：不能为null

### 可选字段
- `productAttributeCategoryId` - 商品属性分类ID
- `subTitle` - 副标题
- `pic` - 商品主图
- `albumPics` - 商品相册
- `description` - 商品描述
- `originalPrice` - 原价
- `unit` - 单位
- `weight` - 重量
- `publishStatus` - 上架状态
- `sort` - 排序
- `keywords` - 关键词
- `note` - 备注
- `detailTitle` - 详情标题
- `detailDesc` - 详情描述
- `detailHtml` - 详情HTML

---

## 🎯 测试场景检查清单

### 创建商品测试
- [ ] 最小必填字段创建
- [ ] 完整字段创建
- [ ] 缺少productCategoryId
- [ ] 缺少farmerId
- [ ] 缺少name
- [ ] 缺少productSn
- [ ] 缺少price
- [ ] 缺少stock
- [ ] name为空字符串
- [ ] productSn为空字符串

### 查询详情测试
- [ ] 查询存在的商品
- [ ] 查询不存在的商品（返回30001）
- [ ] 查询ID为0（返回30006）
- [ ] 查询ID为负数（返回30006）

### 分页查询测试
- [ ] 查询第一页
- [ ] 按名称模糊查询
- [ ] 按分类查询
- [ ] 按上架状态查询
- [ ] 组合条件查询

### 修改商品测试
- [ ] 修改所有字段
- [ ] 只修改部分字段
- [ ] 缺少id字段
- [ ] 修改不存在的商品

### 删除商品测试
- [ ] 删除存在的商品
- [ ] 删除不存在的商品
- [ ] 重复删除同一商品

---

## 🔗 相关文件位置

| 文件 | 位置 |
|------|------|
| 控制器 | `guimall-module-admin/src/main/java/com/gg/guimall/admin/controller/PmsProductController.java` |
| 服务接口 | `guimall-module-admin/src/main/java/com/gg/guimall/admin/service/PmsProductService.java` |
| 服务实现 | `guimall-module-admin/src/main/java/com/gg/guimall/admin/service/impl/PmsProductServiceImpl.java` |
| 请求VO | `guimall-module-admin/src/main/java/com/gg/guimall/admin/model/vo/pms/` |
| 异常状态码 | `guimall-module-common/src/main/java/com/gg/guimall/common/enums/ResponseCodeEnum.java` |
| 数据库映射 | `guimall-module-common/src/main/java/com/gg/guimall/common/domain/mapper/PmsProductMapper.java` |

---

## 💡 常见问题

**Q: 为什么修改不存在的商品也返回成功？**
A: MyBatis Plus的updateById方法不检查是否存在，直接执行UPDATE语句。如需检查，可在服务层添加验证。

**Q: 删除是物理删除还是逻辑删除？**
A: 是逻辑删除。PmsProductDO中有@TableLogic注解，deleteStatus字段会自动设置为1。

**Q: 分页查询返回的数据结构是什么？**
A: 返回PageResponse对象，包含data（商品列表）、total（总数）、current（当前页）、size（每页大小）、pages（总页数）。

**Q: 如何在Swagger UI中测试？**
A: 访问 `http://localhost:8080/doc.html`，找到商品管理模块，点击API端点进行测试。
