# GuiMall前端Vue3快速参考索引

## 📁 目录结构

### pages目录 
```
guimall-vue3/src/pages/
├── admin/                    # 后台管理系统 (24个页面)
│   ├── index.vue            # 首页
│   ├── login.vue            # 登录
│   ├── farmer/              # 农户管理 (4个)
│   ├── oms/                 # 订单管理 (4个)
│   ├── pms/                 # 商品管理 (7个)
│   ├── sms/                 # 营销管理 (3个)
│   ├── trace/               # 溯源管理 (3个)
│   └── ums/                 # 权限管理 (3个)
│
└── frontend/                # 前台商城 (37个页面)
    ├── cart/                # 购物车
    ├── checkout/            # 结账
    ├── coupon/              # 优惠券
    ├── home/                # 首页
    ├── member/              # 会员中心
    ├── order/               # 订单管理
    ├── pay/                 # 支付页面
    ├── product/             # 商品浏览
    ├── support/             # 助农专区
    ├── trace/               # 溯源查询
    └── user/                # 用户信息
```

### api目录
```
guimall-vue3/src/api/
├── admin/                   # 后台API (25个文件)
│   ├── user.js              # 管理员登录与用户管理
│   ├── product.js           # 商品SPU管理
│   ├── productCategory.js   # 商品分类
│   ├── productAttrCategory.js # 属性分类
│   ├── productAttr.js       # 商品属性项
│   ├── productSku.js        # SKU库存
│   ├── productFullReduction.js # 满减
│   ├── farmer.js            # 农户管理
│   ├── order.js             # 订单管理
│   ├── orderReturnApply.js  # 退货申请
│   ├── orderReturnReason.js # 退货原因
│   ├── coupon.js            # 优惠券
│   ├── couponHistory.js     # 优惠券记录
│   ├── homeAdvertise.js     # 广告管理
│   ├── homeNewProduct.js    # 新品推荐
│   ├── homeRecommendProduct.js # 人气推荐
│   ├── traceOrigin.js       # 产地管理
│   ├── traceProductOrigin.js # 产地-农户关联
│   ├── traceRecord.js       # 溯源记录
│   ├── traceQrcode.js       # 二维码管理
│   ├── paramDefinition.js   # 参数定义 (数据字典)
│   ├── umsAdmin.js          # 账号管理
│   ├── umsRole.js           # 角色管理
│   ├── umsMenu.js           # 菜单管理
│   ├── upload.js            # 文件上传
│   └── index.js             # 聚合导出
│
└── frontend/                # 前台API (6个文件)
    ├── member.js            # 会员管理
    ├── product.js           # 商品浏览
    ├── cart.js              # 购物车
    ├── order.js             # 订单管理
    ├── coupon.js            # 优惠券
    └── orderReturn.js       # 退货申请
```

---

## 🔥 关键API端点速查

### 会员管理
- POST `/member/login` - 登录
- POST `/member/register` - 注册
- GET `/member/info` - 获取信息
- POST `/member/update` - 更新信息
- GET `/member/address/list` - 地址列表
- POST `/member/address/add` - 添加地址

### 商品相关
- POST `/pms/product/list` - 商品列表
- GET `/pms/product/{id}` - 商品详情
- GET `/pms/product/category/tree` - 分类树
- GET `/home/advertises` - 轮播图
- GET `/home/new-products` - 新品推荐
- GET `/home/recommend-products` - 人气推荐

### 购物车
- POST `/cart/add` - 添加商品
- GET `/cart/list` - 购物车列表
- GET `/cart/update/quantity` - 修改数量
- POST `/cart/delete` - 删除商品
- POST `/cart/clear` - 清空购物车

### 订单
- POST `/order/submit` - 提交订单
- POST `/order/list` - 订单列表
- GET `/order/{id}` - 订单详情
- GET `/alipay/pay` - 支付宝支付
- GET `/alipay/query` - 查询支付状态

### 优惠券
- GET `/coupon/available/list` - 可领取优惠券
- POST `/coupon/receive` - 领取优惠券
- GET `/coupon/member/list` - 我的优惠券
- GET `/coupon/order/available/list` - 订单可用优惠券

### 溯源
- GET `/trace/{productId}` - 溯源详情
- GET `/support/farmers` - 帮扶农户

---

## 📊 统计数据

| 指标 | 数值 |
|-----|-----|
| Admin页面总数 | 24个 |
| Frontend页面总数 | 37个 |
| Admin API文件 | 25个 |
| Frontend API文件 | 6个 |
| Admin API函数 | 155+ |
| Frontend API函数 | 47个 |
| **总计API函数** | **200+** |

### API使用率
- **最常用服务**: 商品管理 (PMS) - 51个接口
- **次常用服务**: 用户权限 (UMS) - 76个接口
- **第三多**: 订单管理 (OMS) - 14个接口

### HTTP方法分布
| 方法 | Admin | Frontend | 总计 |
|-----|-------|----------|-----|
| POST | 108 (70%) | 17 (36%) | 125 |
| GET | 31 (20%) | 30 (64%) | 61 |
| PUT | 7 (4.5%) | 0 | 7 |
| DELETE | 9 (5.5%) | 0 | 9 |

---

## 🎯 核心业务流程

### 1️⃣ 前台用户流程
```
会员登录                 → 浏览商品              → 加入购物车
/member/login             /pms/product/*        /cart/add
    ↓                          ↓                      ↓
获取会员信息    →    获取分类树    →    查看购物车
/member/info          /pms/product/category   /cart/list
    ↓
提交订单 → 支付宝支付 → 查询状态 → 订单详情
/order/submit  /alipay/pay  /alipay/query  /order/{id}
```

### 2️⃣ 后台管理流程
```
管理员登录               商品管理              订单处理
/login         →    /admin/pms/product/*   →  /admin/oms/order/*
获取用户信息              获取详情                查看订单
/admin/user/info      编辑商品SKU              发货/关闭
                    保存上架/下架              处理退货
```

---

## 🔗 页面与API对应关系

### 前台首页 (frontend/home/index.vue)
```javascript
调用API:
- getHomeAdvertises() → GET /home/advertises
- getCategoryTree() → GET /pms/product/category/tree
- getProductList() → POST /pms/product/list
- getNewProducts() → GET /home/new-products
- getRecommendProducts() → GET /home/recommend-products
```

### 商品详情页 (frontend/product/detail.vue)
```javascript
调用API:
- getProductDetail(id) → GET /pms/product/{id}
- getTraceDetail(productId) → GET /trace/{productId}
- getSupportFarmers() → GET /support/farmers
```

### 会员登录页 (frontend/member/login.vue)
```javascript
调用API:
- memberLogin(data) → POST /member/login
- memberRegister(data) → POST /member/register
```

### 后台商品列表 (admin/pms/product/list.vue)
```javascript
调用API:
- fetchProductList(data) → POST /admin/pms/product/list
- createProduct(data) → POST /admin/pms/product/create
- updateProduct(data) → POST /admin/pms/product/update
- deleteProduct(id) → DELETE /admin/pms/product/{id}
- publishProduct(id) → PUT /admin/pms/product/{id}/publish
```

---

## ⚡ 快速定位API

### 需要修改/查看某个API？

- 🔵 **会员相关** → 查看 `api/frontend/member.js`
- 🟢 **商品相关** → 查看 `api/frontend/product.js` 或 `api/admin/product.js`
- 🟡 **购物车相关** → 查看 `api/frontend/cart.js`
- 🔴 **订单相关** → 查看 `api/frontend/order.js` 或 `api/admin/order.js`
- 🟣 **优惠券相关** → 查看 `api/frontend/coupon.js` 或 `api/admin/coupon.js`
- 🟤 **权限相关** → 查看 `api/admin/umsAdmin.js` / `umsRole.js` / `umsMenu.js`
- ⬜ **文件上传** → 查看 `api/admin/upload.js`
- ⚫ **溯源相关** → 查看 `api/admin/traceOrigin.js` 等

---

## 📝 开发建议

### 1. API使用约定
- ✅ Frontend调用 `/api/frontend/` 中的API
- ✅ Admin调用 `/api/admin/` 中的API
- ✅ 导出统一在 `api/admin/index.js` 中进行

### 2. 新增API步骤
1. 在对应的 API 文件中添加函数
2. 在页面中导入和使用
3. 更新本文档

### 3. API命名规范
- 查询列表: `fetch*List` 或 `get*List`
- 获取详情: `get*Detail` 或 `get*`
- 创建: `create*`
- 修改: `update*`
- 删除: `delete*`

### 4. 错误排查
- API 404? → 检查后端Controller是否存在
- 参数错误? → 参考API文档的params/data字段
- 跨域问题? → 检查axios配置 (src/axios.js)

---

## 📚 相关文档

- 📄 [完整API分析报告](./前端Vue3页面和API分析报告.md)
- 📄 [API接口完整列表](./API_INTERFACE_LIST.md)
- 📁 页面目录: `src/pages/`
- 📁 API目录: `src/api/`

---

*最后更新: 2026年4月8日*
*维护者: GuiMall项目组*
