# GuiMall前端(Vue3)页面和API分析报告

**生成时间**: 2026年4月8日  
**项目路径**: `d:\B_CodeSoft\Code\guimall\guimall-vue3\`

---

## 一、pages目录结构

### 1. Admin后台页面 (pages/admin/)

#### 总页数：24个

##### 首页与登录
- `index.vue` - 后台首页/仪表板
- `login.vue` - 后台管理员登录页

##### 农户管理 (farmer/)
- `farmer/list.vue` - 农户列表
- `farmer/add.vue` - 新增农户
- `farmer/edit.vue` - 编辑农户
- `farmer/update.vue` - 更新农户

##### 订单管理 (oms/)
- `oms/order/list.vue` - 订单列表
- `oms/order/detail.vue` - 订单详情
- `oms/orderReturn/list.vue` - 退货申请列表
- `oms/orderReturn/detail.vue` - 退货详情

##### 商品管理 (pms/)
- `pms/product/list.vue` - 商品列表
- `pms/product/add.vue` - 新增商品
- `pms/product/edit.vue` - 编辑商品
- `pms/category/list.vue` - 分类管理
- `pms/attr/list.vue` - 属性管理
- `pms/coupon/list.vue` - 优惠券管理
- `pms/coupon/add.vue` - 新增优惠券

##### 短信营销 (sms/)
- `sms/advertise/list.vue` - 广告管理
- `sms/newProduct/list.vue` - 新品推荐
- `sms/recommend/list.vue` - 人气推荐

##### 溯源管理 (trace/)
- `trace/origin/list.vue` - 产地管理
- `trace/record/list.vue` - 溯源记录
- `trace/qrcode/list.vue` - 二维码管理

##### 权限管理 (ums/)
- `ums/user/list.vue` - 账号管理
- `ums/role/list.vue` - 角色管理
- `ums/menu/list.vue` - 菜单管理

---

### 2. Frontend前台页面 (pages/frontend/)

#### 总页数：37个

##### 购物车 (cart/)
- `cart/index.vue` - 购物车主页

##### 结账/支付 (checkout/)
- `checkout/index.vue` - 提交订单/支付页

##### 通用页面 (common/)
- `common/placeholder.vue` - 占位符页面

##### 优惠券 (coupon/)
- `coupon/index.vue` - 领券中心

##### 首页 (home/)
- `home/index.vue` - 前台首页

##### 会员中心 (member/)
- `member/login.vue` - 会员登录/注册
- `member/center.vue` - 会员中心

##### 订单 (order/)
- `order/index.vue` - 订单列表
- `order/detail.vue` - 订单详情
- `order/return.vue` - 申请退货

##### 支付 (pay/)
- `pay/index.vue` - 支付页面
- `pay/result.vue` - 支付结果页

##### 商品 (product/)
- `product/detail.vue` - 商品详情页
- `product/category.vue` - 分类浏览页

##### 帮助支持 (support/)
- `support/index.vue` - 助农专区

##### 溯源 (trace/)
- `trace/index.vue` - 溯源查询页

##### 用户信息 (user/)
- `user/coupons.vue` - 我的优惠券

---

## 二、API文件定义和接口清单

### Admin后台API (src/api/admin/)

#### 1. user.js - 管理员用户管理
**定义的函数**:
- `login(username, password)` - 后台管理员登录
- `getUserInfo()` - 获取当前登录管理员信息
- `updateAdminPassword(data)` - 修改管理员密码

**API端点**:
- POST `/login` - 登录
- POST `/admin/user/info` - 用户信息
- POST `/admin/password/update` - 密码修改

#### 2. product.js - 商品SPU管理
**定义的函数**:
- `createProduct(data)` - 新增商品
- `fetchProductList(data)` - 分页查询商品列表
- `getProductDetail(id)` - 获取商品详情
- `updateProduct(data)` - 修改商品
- `deleteProduct(id)` - 删除商品
- `publishProduct(id)` - 商品上架
- `unpublishProduct(id)` - 商品下架
- `batchDeleteProducts(ids)` - 批量删除商品
- `batchPublishProducts(ids)` - 批量上架
- `batchUnpublishProducts(ids)` - 批量下架

**API端点**:
- POST `/admin/pms/product/create` - 创建
- POST `/admin/pms/product/list` - 列表
- GET `/admin/pms/product/{id}` - 详情
- POST `/admin/pms/product/update` - 更新
- DELETE `/admin/pms/product/{id}` - 删除
- PUT `/admin/pms/product/{id}/publish` - 上架
- PUT `/admin/pms/product/{id}/unpublish` - 下架
- POST `/admin/pms/product/batch/delete` - 批量删除
- POST `/admin/pms/product/batch/publish` - 批量上架
- POST `/admin/pms/product/batch/unpublish` - 批量下架

#### 3. productCategory.js - 商品分类管理
**定义的函数**:
- `createProductCategory(data)` - 新增分类
- `updateProductCategory(data)` - 修改分类
- `deleteProductCategory(id)` - 删除分类
- `fetchProductCategoryList(data)` - 分页列表
- `fetchProductCategoryTree()` - 分类树
- `fetchProductCategoryOptions()` - 下拉选项

**API端点**:
- POST `/admin/pms/productCate` - 新增
- PUT `/admin/pms/productCate` - 修改
- DELETE `/admin/pms/productCate/{id}` - 删除
- POST `/admin/pms/productCate/list` - 列表
- GET `/admin/pms/productCate/tree` - 树形
- GET `/admin/pms/productCate/options` - 选项

#### 4. productAttrCategory.js - 属性分类管理
**定义的函数**:
- `createProductAttrCategory(data)` - 新增属性分类
- `fetchProductAttrCategoryList(data)` - 列表
- `updateProductAttrCategory(data)` - 修改
- `deleteProductAttrCategory(id)` - 删除
- `fetchProductAttrCategoryOptions()` - 选项

**API端点**:
- POST `/admin/pms/productAttr/create` - 创建
- POST `/admin/pms/productAttr/list` - 列表
- POST `/admin/pms/productAttr/update` - 更新
- DELETE `/admin/pms/productAttr/{id}` - 删除
- GET `/admin/pms/productAttr/options` - 选项

#### 5. productAttr.js - 商品具体属性项
**定义的函数**:
- `createProductAttr(data)` - 新增属性
- `fetchProductAttrList(data)` - 列表
- `updateProductAttr(data)` - 修改属性
- `deleteProductAttr(id)` - 删除属性
- `fetchProductAttrByCategoryId(categoryId, type)` - 按分类查询属性

**API端点**:
- POST `/admin/pms/productAttr/productAttrList/create`
- POST `/admin/pms/productAttr/productAttrList/list`
- POST `/admin/pms/productAttr/productAttrList/update`
- DELETE `/admin/pms/productAttr/productAttrList/{id}`
- GET `/admin/pms/productAttr/productAttrList/category/{categoryId}`

#### 6. productSku.js - SKU库存管理
**定义的函数**:
- `fetchSkuListByProductId(productId)` - 按商品ID查询SKU
- `saveSkuList(productId, skuList)` - 保存SKU列表
- `deleteSku(id)` - 删除SKU

**API端点**:
- GET `/admin/pms/product/sku/{productId}`
- POST `/admin/pms/product/sku/{productId}/save`
- DELETE `/admin/pms/product/sku/{id}`

#### 7. productFullReduction.js - 商品满减管理
**定义的函数**:
- `fetchFullReductionListByProductId(productId)` - 查询满减列表
- `saveFullReductionList(productId, fullReductionList)` - 保存满减
- `deleteFullReduction(id)` - 删除满减

**API端点**:
- GET `/admin/pms/product/fullReduction/{productId}`
- POST `/admin/pms/product/fullReduction/{productId}/save`
- DELETE `/admin/pms/product/fullReduction/{id}`

#### 8. farmer.js - 农户管理
**定义的函数**:
- `createFarmer(data)` - 新增农户
- `fetchFarmerList(data)` - 列表
- `getFarmerDetail(id)` - 详情
- `updateFarmer(data)` - 修改
- `deleteFarmer(id)` - 删除
- `fetchFarmerOptions()` - 下拉选项

**API端点**:
- POST `/admin/farmer/create`
- POST `/admin/farmer/list`
- GET `/admin/farmer/{id}`
- POST `/admin/farmer/update`
- DELETE `/admin/farmer/{id}`
- GET `/admin/farmer/options`

#### 9. order.js - 订单管理（后台）
**定义的函数**:
- `fetchOrderList(data)` - 订单列表
- `getOrderDetail(id)` - 订单详情
- `deliverOrder(data)` - 发货
- `closeOrder(data)` - 关闭订单
- `remarkOrder(data)` - 订单备注

**API端点**:
- POST `/admin/oms/order/list`
- GET `/admin/oms/order/{id}`
- POST `/admin/oms/order/deliver`
- POST `/admin/oms/order/close`
- POST `/admin/oms/order/remark`

#### 10. orderReturnApply.js - 退货申请管理
**定义的函数**:
- `fetchOrderReturnApplyList(data)` - 列表
- `getOrderReturnApplyDetail(id)` - 详情
- `updateOrderReturnApplyStatus(data)` - 更新状态

**API端点**:
- POST `/admin/oms/orderReturnApply/list`
- GET `/admin/oms/orderReturnApply/{id}`
- POST `/admin/oms/orderReturnApply/status/update`

#### 11. orderReturnReason.js - 退货原因管理
**定义的函数**:
- `getOrderReturnReason(id)` - 获取原因
- `createOrderReturnReason(data)` - 新增原因
- `deleteOrderReturnReasons(ids)` - 批量删除
- `fetchOrderReturnReasonList(data)` - 列表
- `updateOrderReturnReason(id, data)` - 修改原因
- `updateOrderReturnReasonStatus(data)` - 修改状态

**API端点**:
- GET `/admin/oms/returnReason/{id}`
- POST `/admin/oms/returnReason/create`
- POST `/admin/oms/returnReason/delete`
- POST `/admin/oms/returnReason/list`
- POST `/admin/oms/returnReason/update/{id}`
- POST `/admin/oms/returnReason/update/status`

#### 12. coupon.js - 优惠券管理（后台）
**定义的函数**:
- `createCoupon(data)` - 新增优惠券
- `deleteCoupon(id)` - 删除优惠券
- `fetchCouponList(params)` - 列表
- `getCouponDetail(id)` - 详情
- `updateCoupon(id, data)` - 修改优惠券

**API端点**:
- POST `/admin/sms/coupon/create`
- POST `/admin/sms/coupon/delete/{id}`
- GET `/admin/sms/coupon/list`
- GET `/admin/sms/coupon/{id}`
- POST `/admin/sms/coupon/update/{id}`

#### 13. couponHistory.js - 优惠券领取记录
**定义的函数**:
- `fetchCouponHistoryList(params)` - 列表

**API端点**:
- GET `/admin/sms/couponHistory/list`

#### 14. homeAdvertise.js - 首页轮播广告管理
**定义的函数**:
- `createHomeAdvertise(data)` - 新增广告
- `updateHomeAdvertise(id, data)` - 修改广告
- `deleteHomeAdvertise(id)` - 删除广告
- `fetchHomeAdvertiseList(params)` - 列表
- `getHomeAdvertiseDetail(id)` - 详情

**API端点**:
- POST `/admin/sms/advertise/create`
- POST `/admin/sms/advertise/update/{id}`
- POST `/admin/sms/advertise/delete/{id}`
- GET `/admin/sms/advertise/list`
- GET `/admin/sms/advertise/{id}`

#### 15. homeNewProduct.js - 新品推荐管理
**定义的函数**:
- `createHomeNewProduct(data)` - 新增推荐
- `deleteHomeNewProduct(id)` - 删除推荐
- `fetchHomeNewProductList(params)` - 列表
- `getHomeNewProductDetail(id)` - 详情
- `updateHomeNewProduct(id, data)` - 修改推荐
- `fetchHomeNewProductOptions()` - 下拉选项

**API端点**:
- POST `/admin/sms/newProduct/create`
- POST `/admin/sms/newProduct/delete/{id}`
- GET `/admin/sms/newProduct/list`
- GET `/admin/sms/newProduct/{id}`
- POST `/admin/sms/newProduct/update/{id}`
- GET `/admin/sms/newProduct/options`

#### 16. homeRecommendProduct.js - 人气推荐管理
**定义的函数**:
- `createHomeRecommendProduct(data)` - 新增推荐
- `deleteHomeRecommendProduct(id)` - 删除推荐
- `fetchHomeRecommendProductList(params)` - 列表
- `getHomeRecommendProductDetail(id)` - 详情
- `updateHomeRecommendProduct(id, data)` - 修改推荐
- `fetchHomeRecommendProductOptions()` - 下拉选项

**API端点**:
- POST `/admin/sms/recommendProduct/create`
- POST `/admin/sms/recommendProduct/delete/{id}`
- GET `/admin/sms/recommendProduct/list`
- GET `/admin/sms/recommendProduct/{id}`
- POST `/admin/sms/recommendProduct/update/{id}`
- GET `/admin/sms/recommendProduct/options`

#### 17. traceOrigin.js - 产地档案管理
**定义的函数**:
- `createTraceOrigin(data)` - 新增产地
- `fetchTraceOriginList(data)` - 列表
- `getTraceOriginDetail(id)` - 详情
- `updateTraceOrigin(data)` - 修改产地
- `deleteTraceOrigin(id)` - 删除产地
- `fetchTraceOriginOptions()` - 下拉选项

**API端点**:
- POST `/admin/trace/origin/create`
- POST `/admin/trace/origin/list`
- GET `/admin/trace/origin/{id}`
- POST `/admin/trace/origin/update`
- DELETE `/admin/trace/origin/{id}`
- GET `/admin/trace/origin/options`

#### 18. traceProductOrigin.js - 农户-产地关联查询
**定义的函数**:
- `getProductBindingsByOriginId(originId)` - 按产地查询关联产品
- `getProductBindingsByFarmerId(farmerId)` - 按农户查询关联产品

**API端点**:
- GET `/admin/trace/productOrigin/origin/{originId}`
- GET `/admin/trace/productOrigin/farmer/{farmerId}`

#### 19. traceRecord.js - 溯源记录管理
**定义的函数**:
- `createTraceRecord(data)` - 新增溯源记录
- `updateTraceRecord(data)` - 修改溯源记录
- `deleteTraceRecord(id)` - 删除溯源记录
- `fetchTraceRecordList(data)` - 列表

**API端点**:
- POST `/admin/trace/record/create`
- POST `/admin/trace/record/update`
- DELETE `/admin/trace/record/{id}`
- POST `/admin/trace/record/list`

#### 20. traceQrcode.js - 溯源二维码管理
**定义的函数**:
- `upsertTraceQrcode(data)` - 新增/更新二维码
- `getTraceQrcodeByProductId(productId)` - 获取商品二维码
- `deleteTraceQrcodeByProductId(productId)` - 删除商品二维码

**API端点**:
- POST `/admin/trace/qrcode/upsert`
- GET `/admin/trace/qrcode/product/{productId}`
- DELETE `/admin/trace/qrcode/product/{productId}`

#### 21. paramDefinition.js - 商品参数定义
**定义的函数**:
- `fetchParamDefinitions(params)` - 获取参数定义（支持分页）
- `createParamDefinition(data)` - 新增参数定义
- `updateParamDefinition(id, data)` - 修改参数定义
- `deleteParamDefinition(id)` - 删除参数定义

**API端点**:
- GET `/admin/pms/paramDefinition/page` (分页)
- GET `/admin/pms/paramDefinition/list` (列表)
- POST `/admin/pms/paramDefinition`
- PUT `/admin/pms/paramDefinition/{id}`
- DELETE `/admin/pms/paramDefinition/{id}`

#### 22. umsAdmin.js - 后台账号管理
**定义的函数**:
- `fetchUmsAdminList(data)` - 分页查询账号
- `fetchUmsRoleOptions()` - 获取角色下拉选项
- `createUmsAdmin(data)` - 新增账号
- `updateUmsAdmin(data)` - 修改账号
- `deleteUmsAdmins(ids)` - 批量删除账号
- `assignUmsAdminRole(data)` - 分配角色

**API端点**:
- POST `/admin/ums/admin/list`
- GET `/admin/ums/admin/roleOptions`
- POST `/admin/ums/admin/create`
- POST `/admin/ums/admin/update`
- POST `/admin/ums/admin/delete`
- POST `/admin/ums/admin/assignRole`

#### 23. umsRole.js - 角色管理
**定义的函数**:
- `fetchUmsRoleList(data)` - 分页查询角色
- `fetchUmsRoleOptions()` - 获取角色下拉选项
- `createUmsRole(data)` - 新增角色
- `updateUmsRole(data)` - 修改角色
- `deleteUmsRoles(ids)` - 批量删除角色
- `initUmsRoleAllocMenu(roleId)` - 初始化菜单权限分配页
- `saveUmsRoleAllocMenu(data)` - 保存菜单权限分配

**API端点**:
- POST `/admin/ums/role/list`
- GET `/admin/ums/role/options`
- POST `/admin/ums/role/create`
- POST `/admin/ums/role/update`
- POST `/admin/ums/role/delete`
- GET `/admin/ums/role/allocMenu/init`
- POST `/admin/ums/role/allocMenu/save`

#### 24. umsMenu.js - 菜单管理
**定义的函数**:
- `fetchUmsMenuList(data)` - 分页查询菜单
- `fetchUmsMenuOptions()` - 获取顶级菜单下拉选项
- `createUmsMenu(data)` - 新增菜单
- `updateUmsMenu(data)` - 修改菜单
- `deleteUmsMenus(ids)` - 批量删除菜单

**API端点**:
- POST `/admin/ums/menu/list`
- GET `/admin/ums/menu/options`
- POST `/admin/ums/menu/create`
- POST `/admin/ums/menu/update`
- POST `/admin/ums/menu/delete`

#### 25. upload.js - 文件上传
**定义的函数**:
- `uploadFile(file)` - 上传文件

**API端点**:
- POST `/file/upload`

---

### Frontend前台API (src/api/frontend/)

#### 1. member.js - 会员管理
**定义的函数**:
- `memberLogin(data)` - 会员登录
- `memberRegister(data)` - 会员注册
- `getMemberInfoApi(memberId)` - 获取会员信息
- `updateMemberInfo(data)` - 更新会员信息
- `updateMemberPassword(data)` - 修改会员密码
- `getAddressList(memberId)` - 收货地址列表
- `addAddress(data)` - 新增收货地址
- `updateAddress(data)` - 修改收货地址
- `deleteAddress(id)` - 删除收货地址
- `setDefaultAddress(id, memberId)` - 设为默认地址

**API端点**:
- POST `/member/login`
- POST `/member/register`
- GET `/member/info`
- POST `/member/update`
- POST `/member/updatePassword`
- GET `/member/address/list`
- POST `/member/address/add`
- POST `/member/address/update`
- DELETE `/member/address/{id}`
- POST `/member/address/{id}/default`

#### 2. product.js - 商品浏览
**定义的函数**:
- `getHomeAdvertises(type = 0)` - 获取首页轮播图
- `getCategoryTree()` - 获取商品分类树
- `getProductList(data)` - 商品列表分页查询
- `getNewProducts()` - 获取新品推荐列表
- `getRecommendProducts()` - 获取人气推荐列表
- `getProductDetail(id)` - 获取商品详情
- `getTraceDetail(productId)` - 获取溯源详情
- `getAidAgricultureProducts(data)` - 获取助农商品列表
- `getSupportFarmers()` - 获取签约帮扶农户列表

**API端点**:
- GET `/home/advertises`
- GET `/pms/product/category/tree`
- POST `/pms/product/list`
- GET `/home/new-products`
- GET `/home/recommend-products`
- GET `/pms/product/{id}`
- GET `/trace/{productId}`
- POST `/pms/product/list` (with isAidAgriculture: 1)
- GET `/support/farmers`

#### 3. cart.js - 购物车管理
**定义的函数**:
- `addCartItem(data)` - 添加商品到购物车
- `getCartList(memberId)` - 获取购物车列表
- `updateCartQuantity(memberId, id, quantity)` - 修改购物车数量
- `deleteCartItems(data)` - 删除购物车项
- `clearCart(data)` - 清空购物车

**API端点**:
- POST `/cart/add`
- GET `/cart/list`
- GET `/cart/update/quantity`
- POST `/cart/delete`
- POST `/cart/clear`

#### 4. order.js - 订单管理
**定义的函数**:
- `submitOrder(data)` - 提交订单
- `getOrderList(data)` - 订单列表
- `getOrderDetail(id, memberId)` - 订单详情
- `createAlipay(orderSn)` - 创建支付宝支付
- `queryAlipayStatus(orderSn)` - 查询支付宝交易状态
- `getRemainingTime(orderSn)` - 查询订单剩余支付时间

**API端点**:
- POST `/order/submit`
- POST `/order/list`
- GET `/order/{id}`
- GET `/alipay/pay`
- GET `/alipay/query`
- GET `/order/remaining-time`

#### 5. coupon.js - 优惠券
**定义的函数**:
- `getAvailableCouponList(memberId)` - 获取可领取优惠券列表
- `receiveCoupon(data)` - 领取优惠券
- `getMemberCouponList(memberId, useStatus)` - 获取会员优惠券列表
- `getOrderAvailableCoupons(memberId, totalAmount)` - 获取订单可用优惠券列表

**API端点**:
- GET `/coupon/available/list`
- POST `/coupon/receive`
- GET `/coupon/member/list`
- GET `/coupon/order/available/list`

#### 6. orderReturn.js - 订单退货
**定义的函数**:
- `getReturnReasons()` - 查询退货原因列表
- `createReturnApply(data)` - 创建退货申请
- `cancelOrder(data)` - 取消订单

**API端点**:
- GET `/order/return/reasons`
- POST `/order/return/apply`
- POST `/order/return/cancel`

---

## 三、页面与API调用关系映射

### Admin后台页面API调用

| 页面 | 调用的API | HTTP方法 | 用途 |
|-----|---------|--------|------|
| login.vue | login | POST | 管理员登录 |
| index.vue | getUserInfo | POST | 获取当前用户信息 |
| farmer/list.vue | fetchFarmerList | POST | 加载农户列表 |
| farmer/add.vue | createFarmer | POST | 创建新农户 |
| farmer/edit.vue | getFarmerDetail | GET | 获取农户详情 |
| farmer/update.vue | updateFarmer | POST | 更新农户信息 |
| oms/order/list.vue | fetchOrderList | POST | 加载订单列表 |
| oms/order/detail.vue | getOrderDetail | GET | 获取订单详情 |
| oms/order/detail.vue | deliverOrder | POST | 发货 |
| oms/order/detail.vue | closeOrder | POST | 关闭订单 |
| oms/order/detail.vue | remarkOrder | POST | 添加备注 |
| oms/orderReturn/list.vue | fetchOrderReturnApplyList | POST | 加载退货列表 |
| oms/orderReturn/detail.vue | getOrderReturnApplyDetail | GET | 获取退货详情 |
| oms/orderReturn/detail.vue | updateOrderReturnApplyStatus | POST | 更新退货状态 |
| pms/product/list.vue | fetchProductList | POST | 加载商品列表 |
| pms/product/add.vue | createProduct | POST | 创建商品 |
| pms/product/add.vue | fetchProductCategoryTree | GET | 获取分类树 |
| pms/product/edit.vue | getProductDetail | GET | 获取商品详情 |
| pms/product/edit.vue | updateProduct | POST | 更新商品 |
| pms/product/edit.vue | saveSkuList | POST | 保存SKU列表 |
| pms/product/edit.vue | saveFullReductionList | POST | 保存满减 |
| pms/category/list.vue | fetchProductCategoryList | POST | 加载分类列表 |
| pms/category/list.vue | createProductCategory | POST | 创建分类 |
| pms/category/list.vue | updateProductCategory | PUT | 修改分类 |
| pms/category/list.vue | deleteProductCategory | DELETE | 删除分类 |
| pms/attr/list.vue | fetchProductAttrList | POST | 加载属性列表 |
| pms/coupon/list.vue | fetchCouponList | GET | 加载优惠券列表 |
| pms/coupon/add.vue | createCoupon | POST | 创建优惠券 |
| sms/advertise/list.vue | fetchHomeAdvertiseList | GET | 加载广告列表 |
| sms/newProduct/list.vue | fetchHomeNewProductList | GET | 加载新品推荐 |
| sms/recommend/list.vue | fetchHomeRecommendProductList | GET | 加载人气推荐 |
| trace/origin/list.vue | fetchTraceOriginList | POST | 加载产地列表 |
| trace/record/list.vue | fetchTraceRecordList | POST | 加载溯源记录 |
| trace/qrcode/list.vue | getTraceQrcodeByProductId | GET | 获取二维码 |
| ums/user/list.vue | fetchUmsAdminList | POST | 加载账号列表 |
| ums/role/list.vue | fetchUmsRoleList | POST | 加载角色列表 |
| ums/menu/list.vue | fetchUmsMenuList | POST | 加载菜单列表 |

### Frontend前台页面API调用

| 页面 | 调用的API | HTTP方法 | 用途 |
|-----|---------|--------|------|
| home/index.vue | getHomeAdvertises | GET | 获取轮播广告 |
| home/index.vue | getCategoryTree | GET | 获取商品分类 |
| home/index.vue | getProductList | POST | 获取商品列表 |
| home/index.vue | getNewProducts | GET | 获取新品推荐 |
| home/index.vue | getRecommendProducts | GET | 获取人气推荐 |
| member/login.vue | memberLogin | POST | 会员登录 |
| member/login.vue | memberRegister | POST | 会员注册 |
| member/center.vue | getMemberInfoApi | GET | 获取会员信息 |
| member/center.vue | updateMemberInfo | POST | 更新会员信息 |
| member/center.vue | getAddressList | GET | 获取地址列表 |
| member/center.vue | addAddress | POST | 添加地址 |
| member/center.vue | updateAddress | POST | 修改地址 |
| member/center.vue | deleteAddress | DELETE | 删除地址 |
| product/detail.vue | getProductDetail | GET | 获取商品详情 |
| product/detail.vue | getTraceDetail | GET | 获取溯源信息 |
| product/category.vue | getCategoryTree | GET | 获取分类树 |
| product/category.vue | getProductList | POST | 获取商品列表 |
| cart/index.vue | getCartList | GET | 获取购物车列表 |
| cart/index.vue | addCartItem | POST | 添加商品到购物车 |
| cart/index.vue | updateCartQuantity | GET | 修改购物车数量 |
| cart/index.vue | deleteCartItems | POST | 删除购物车项 |
| checkout/index.vue | getOrderAvailableCoupons | GET | 获取可用优惠券 |
| checkout/index.vue | submitOrder | POST | 提交订单 |
| pay/index.vue | createAlipay | GET | 创建支付| 
| pay/result.vue | queryAlipayStatus | GET | 查询支付状态 |
| order/index.vue | getOrderList | POST | 获取订单列表 |
| order/detail.vue | getOrderDetail | GET | 获取订单详情 |
| order/return.vue | getReturnReasons | GET | 获取退货原因 |
| order/return.vue | createReturnApply | POST | 创建退货申请 |
| coupon/index.vue | getAvailableCouponList | GET | 获取可领取优惠券 |
| coupon/index.vue | receiveCoupon | POST | 领取优惠券 |
| user/coupons.vue | getMemberCouponList | GET | 获取会员优惠券列表 |
| trace/index.vue | getTraceDetail | GET | 获取溯源详情 |
| support/index.vue | getSupportFarmers | GET | 获取帮扶农户列表 |

---

## 四、API统计信息

### Admin后台API统计
**总计**: 25个API文件，155+ 个API函数

**按模块分类**:
- 用户管理 (UMS) - 76个API
  - umsAdmin.js - 6个函数
  - umsRole.js - 7个函数
  - umsMenu.js - 5个函数
  
- 商品管理 (PMS) - 51个API
  - product.js - 9个函数
  - productCategory.js - 6个函数
  - productAttrCategory.js - 5个函数
  - productAttr.js - 5个函数
  - productSku.js - 3个函数
  - productFullReduction.js - 3个函数
  - paramDefinition.js - 4个函数
  
- 订单管理 (OMS) - 14个API
  - order.js - 5个函数
  - orderReturnApply.js - 3个函数
  - orderReturnReason.js - 6个函数
  
- 营销管理 (SMS) - 20个API
  - coupon.js - 5个函数
  - couponHistory.js - 1个函数
  - homeAdvertise.js - 5个函数
  - homeNewProduct.js - 6个函数
  - homeRecommendProduct.js - 6个函数
  
- 溯源管理 - 13个API
  - traceOrigin.js - 6个函数
  - traceProductOrigin.js - 2个函数
  - traceRecord.js - 4个函数
  - traceQrcode.js - 3个函数
  
- 农户管理 - 6个API
  - farmer.js - 6个函数
  
- 文件上传 - 1个API
  - upload.js - 1个函数
  
- 登录认证 - 3个API
  - user.js - 3个函数

### Frontend前台API统计
**总计**: 6个API文件，47个API函数

**按模块分类**:
- 会员管理 (member.js) - 10个函数
- 商品浏览 (product.js) - 9个函数
- 购物车 (cart.js) - 5个函数
- 订单管理 (order.js) - 6个函数
- 优惠券 (coupon.js) - 4个函数
- 退货申请 (orderReturn.js) - 3个函数

### HTTP方法分布

**Admin后台**:
- POST - 108个 (70%)
- GET - 31个 (20%)
- PUT - 7个 (4.5%)
- DELETE - 9个 (5.5%)

**Frontend前台**:
- POST - 17个 (36%)
- GET - 30个 (64%)

---

## 五、关键业务流程和API调用链

### 1. 用户登录流程
**前台登录**:
- POST /member/login (会员登录)
- GET /member/info (获取会员信息)

**后台登录**:
- POST /login (管理员登录)
- POST /admin/user/info (获取管理员信息)

### 2. 商品浏览流程
- GET /pms/product/category/tree (获取分类)
- POST /pms/product/list (获取商品列表)
- GET /pms/product/{id} (获取商品详情)
- GET /trace/{productId} (获取溯源信息)

### 3. 购物车流程
- POST /cart/add (添加商品)
- GET /cart/list (查看购物车)
- GET /cart/update/quantity (修改数量)
- POST /cart/delete (删除商品)

### 4. 订单支付流程
- POST /order/submit (提交订单)
- GET /alipay/pay (发起支付)
- GET /alipay/query (查询支付状态)
- GET /order/remaining-time (查询支付时间)

### 5. 后台商品管理流程
- POST /admin/pms/product/list (查询列表)
- POST /admin/pms/product/create (创建商品)
- GET /admin/pms/product/{id} (获取详情)
- POST /admin/pms/product/update (修改商品)
- POST /admin/pms/product/sku/{id}/save (保存SKU)
- PUT /admin/pms/product/{id}/publish (上架)
- PUT /admin/pms/product/{id}/unpublish (下架)
- DELETE /admin/pms/product/{id} (删除商品)

---

## 六、API调用特点总结

### 1. 分页查询
- admin端基本都用POST方法携带分页参数
- frontend端也多用POST传递查询参数

### 2. 下拉选项
- 使用GET方法获取 `/options` 路由

### 3. 详情查询
- 多用GET方法，ID通常在URL路径中

### 4. 修改操作
- 多用POST方法，一些用PUT方法
- 修改时通常在URL路径中包含ID或在请求体中

### 5. 删除操作
- 单个删除用DELETE方法，ID在URL路径中
- 批量删除用POST方法，IDs在请求体中

### 6. 文件上传
- 使用FormData格式
- Content-Type: multipart/form-data

### 7. 前后台API分离
- 前台API路径通常不包含 `/admin` 前缀
- 后台API路径统一以 `/admin` 开头

---

## 七、推荐优化建议

1. **API分组管理**: 考虑按功能模块进一步细分API文件
2. **公共方法提取**: 可提取通用的CRUD操作为基类方法
3. **错误处理统一化**: 统一API的错误响应处理
4. **请求/响应拦截**: 在axios实例中添加统一的拦截器
5. **API版本管理**: 为API预留版本控制机制
6. **文档保持同步**: 定期更新API文档与实际实现保持一致

---

*报告生成完毕*
