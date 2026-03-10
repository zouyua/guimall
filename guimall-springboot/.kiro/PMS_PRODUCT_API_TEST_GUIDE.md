# 商品管理API测试指南

## 1. 创建商品 API

**端点**: `POST /admin/product/create`

### 必填字段验证
```json
{
  "productCategoryId": 1,
  "farmerId": 1,
  "name": "新鲜苹果",
  "productSn": "APPLE-001",
  "price": 9.99,
  "stock": 100
}
```

### 测试用例

#### 1.1 正常创建 - 最小必填字段
**输入**:
```json
{
  "productCategoryId": 1,
  "farmerId": 1,
  "name": "新鲜苹果",
  "productSn": "APPLE-001",
  "price": 9.99,
  "stock": 100
}
```
**预期结果**: `success: true`

#### 1.2 完整字段创建
**输入**:
```json
{
  "productCategoryId": 1,
  "productAttributeCategoryId": 1,
  "farmerId": 1,
  "name": "新鲜苹果",
  "subTitle": "来自山东的优质苹果",
  "productSn": "APPLE-001",
  "pic": "http://example.com/apple.jpg",
  "albumPics": "http://example.com/apple1.jpg,http://example.com/apple2.jpg",
  "description": "新鲜苹果，甜度高",
  "price": 9.99,
  "originalPrice": 12.99,
  "stock": 100,
  "unit": "斤",
  "weight": 500,
  "publishStatus": 1,
  "sort": 1,
  "keywords": "苹果,水果",
  "note": "备注信息",
  "detailTitle": "商品详情标题",
  "detailDesc": "商品详情描述",
  "detailHtml": "<p>详情HTML内容</p>"
}
```
**预期结果**: `success: true`

#### 1.3 缺少必填字段 - productCategoryId
**输入**:
```json
{
  "farmerId": 1,
  "name": "新鲜苹果",
  "productSn": "APPLE-001",
  "price": 9.99,
  "stock": 100
}
```
**预期结果**: 
```json
{
  "success": false,
  "errorCode": "10001",
  "message": "productCategoryId 商品分类不能为空, 当前值: 'null';"
}
```

#### 1.4 缺少必填字段 - farmerId
**输入**:
```json
{
  "productCategoryId": 1,
  "name": "新鲜苹果",
  "productSn": "APPLE-001",
  "price": 9.99,
  "stock": 100
}
```
**预期结果**: 
```json
{
  "success": false,
  "errorCode": "10001",
  "message": "farmerId 农户ID不能为空, 当前值: 'null';"
}
```

#### 1.5 缺少必填字段 - name
**输入**:
```json
{
  "productCategoryId": 1,
  "farmerId": 1,
  "productSn": "APPLE-001",
  "price": 9.99,
  "stock": 100
}
```
**预期结果**: 
```json
{
  "success": false,
  "errorCode": "10001",
  "message": "name 商品名称不能为空, 当前值: 'null';"
}
```

#### 1.6 缺少必填字段 - productSn
**输入**:
```json
{
  "productCategoryId": 1,
  "farmerId": 1,
  "name": "新鲜苹果",
  "price": 9.99,
  "stock": 100
}
```
**预期结果**: 
```json
{
  "success": false,
  "errorCode": "10001",
  "message": "productSn 商品货号不能为空, 当前值: 'null';"
}
```

#### 1.7 缺少必填字段 - price
**输入**:
```json
{
  "productCategoryId": 1,
  "farmerId": 1,
  "name": "新鲜苹果",
  "productSn": "APPLE-001",
  "stock": 100
}
```
**预期结果**: 
```json
{
  "success": false,
  "errorCode": "10001",
  "message": "price 商品价格不能为空, 当前值: 'null';"
}
```

#### 1.8 缺少必填字段 - stock
**输入**:
```json
{
  "productCategoryId": 1,
  "farmerId": 1,
  "name": "新鲜苹果",
  "productSn": "APPLE-001",
  "price": 9.99
}
```
**预期结果**: 
```json
{
  "success": false,
  "errorCode": "10001",
  "message": "stock 库存不能为空, 当前值: 'null';"
}
```

---

## 2. 分页查询商品 API

**端点**: `POST /admin/product/list`

### 必填字段
- `current`: 当前页码（继承自BasePageQuery）
- `size`: 每页大小（继承自BasePageQuery）

### 可选字段
- `name`: 商品名称（模糊查询）
- `categoryId`: 分类ID
- `publishStatus`: 上架状态

### 测试用例

#### 2.1 查询第一页，每页10条
**输入**:
```json
{
  "current": 1,
  "size": 10
}
```
**预期结果**: 
```json
{
  "success": true,
  "data": [...],
  "total": 100,
  "current": 1,
  "size": 10,
  "pages": 10
}
```

#### 2.2 按名称模糊查询
**输入**:
```json
{
  "current": 1,
  "size": 10,
  "name": "苹果"
}
```
**预期结果**: 返回名称包含"苹果"的商品列表

#### 2.3 按分类查询
**输入**:
```json
{
  "current": 1,
  "size": 10,
  "categoryId": 1
}
```
**预期结果**: 返回分类ID为1的商品列表

#### 2.4 按上架状态查询
**输入**:
```json
{
  "current": 1,
  "size": 10,
  "publishStatus": 1
}
```
**预期结果**: 返回已上架的商品列表

#### 2.5 组合查询
**输入**:
```json
{
  "current": 1,
  "size": 10,
  "name": "苹果",
  "categoryId": 1,
  "publishStatus": 1
}
```
**预期结果**: 返回符合所有条件的商品列表

---

## 3. 查询商品详情 API

**端点**: `GET /admin/product/{id}`

### 路径参数
- `id`: 商品ID（必填）

### 测试用例

#### 3.1 查询存在的商品
**输入**: `GET /admin/product/1`

**预期结果**:
```json
{
  "success": true,
  "data": {
    "id": 1,
    "productCategoryId": 1,
    "farmerId": 1,
    "name": "新鲜苹果",
    "price": 9.99,
    "stock": 100,
    ...
  }
}
```

#### 3.2 查询不存在的商品
**输入**: `GET /admin/product/99999`

**预期结果**:
```json
{
  "success": false,
  "errorCode": "30001",
  "message": "商品不存在"
}
```

#### 3.3 无效的商品ID（0或负数）
**输入**: `GET /admin/product/0`

**预期结果**:
```json
{
  "success": false,
  "errorCode": "30006",
  "message": "商品数据无效"
}
```

#### 3.4 无效的商品ID（负数）
**输入**: `GET /admin/product/-1`

**预期结果**:
```json
{
  "success": false,
  "errorCode": "30006",
  "message": "商品数据无效"
}
```

---

## 4. 修改商品 API

**端点**: `POST /admin/product/update`

### 必填字段
- `id`: 商品ID
- `productCategoryId`: 商品分类ID
- `farmerId`: 农户ID
- `name`: 商品名称
- `productSn`: 商品货号
- `price`: 销售价格
- `stock`: 库存

### 测试用例

#### 4.1 正常修改
**输入**:
```json
{
  "id": 1,
  "productCategoryId": 1,
  "farmerId": 1,
  "name": "新鲜苹果-已修改",
  "productSn": "APPLE-001",
  "price": 10.99,
  "stock": 150
}
```
**预期结果**: `success: true`

#### 4.2 完整字段修改
**输入**:
```json
{
  "id": 1,
  "productCategoryId": 1,
  "productAttributeCategoryId": 1,
  "farmerId": 1,
  "name": "新鲜苹果-已修改",
  "subTitle": "来自山东的优质苹果-已修改",
  "productSn": "APPLE-001",
  "pic": "http://example.com/apple-new.jpg",
  "albumPics": "http://example.com/apple1-new.jpg",
  "description": "新鲜苹果，甜度高-已修改",
  "price": 10.99,
  "originalPrice": 13.99,
  "stock": 150,
  "unit": "斤",
  "weight": 600,
  "publishStatus": 1,
  "sort": 2,
  "keywords": "苹果,水果,新鲜",
  "note": "备注信息-已修改",
  "detailTitle": "商品详情标题-已修改",
  "detailDesc": "商品详情描述-已修改",
  "detailHtml": "<p>详情HTML内容-已修改</p>"
}
```
**预期结果**: `success: true`

#### 4.3 缺少必填字段 - id
**输入**:
```json
{
  "productCategoryId": 1,
  "farmerId": 1,
  "name": "新鲜苹果",
  "productSn": "APPLE-001",
  "price": 9.99,
  "stock": 100
}
```
**预期结果**: 
```json
{
  "success": false,
  "errorCode": "10001",
  "message": "id 商品ID不能为空, 当前值: 'null';"
}
```

#### 4.4 修改不存在的商品
**输入**:
```json
{
  "id": 99999,
  "productCategoryId": 1,
  "farmerId": 1,
  "name": "新鲜苹果",
  "productSn": "APPLE-001",
  "price": 9.99,
  "stock": 100
}
```
**预期结果**: `success: true`（MyBatis Plus的updateById不会检查是否存在）

---

## 5. 删除商品 API

**端点**: `DELETE /admin/product/{id}`

### 路径参数
- `id`: 商品ID（必填）

### 测试用例

#### 5.1 删除存在的商品
**输入**: `DELETE /admin/product/1`

**预期结果**: `success: true`

#### 5.2 删除不存在的商品
**输入**: `DELETE /admin/product/99999`

**预期结果**: `success: true`（逻辑删除，不存在也返回成功）

#### 5.3 重复删除同一商品
**输入**: `DELETE /admin/product/1` (第二次)

**预期结果**: `success: true`（已被逻辑删除，再次删除不影响）

---

## 异常状态码对照表

| 状态码 | 含义 | 场景 |
|--------|------|------|
| 10001 | 参数错误 | 缺少必填字段、字段验证失败 |
| 30001 | 商品不存在 | 查询详情时商品不存在 |
| 30002 | 商品分类不存在 | 分类ID无效 |
| 30003 | 商品创建失败 | 创建过程中出错 |
| 30004 | 商品修改失败 | 修改过程中出错 |
| 30005 | 商品删除失败 | 删除过程中出错 |
| 30006 | 商品数据无效 | ID为0或负数等无效数据 |

---

## 测试工具推荐

- **Postman**: 用于API测试
- **Swagger UI**: 访问 `http://localhost:8080/doc.html` 查看API文档
- **curl**: 命令行测试

## 测试顺序建议

1. 先测试创建API（1.1 → 1.2 → 1.3-1.8）
2. 再测试查询列表API（2.1 → 2.2-2.5）
3. 然后测试查询详情API（3.1 → 3.2-3.4）
4. 接着测试修改API（4.1 → 4.2 → 4.3-4.4）
5. 最后测试删除API（5.1 → 5.2-5.3）
