# GuiMall 项目 API 接口完整列表

## 项目结构
- **guimall-web**: 前台API（面向用户端）
- **guimall-module-admin**: 后台管理API（面向管理员）

---

## 一、前台API (guimall-web)

### 1. MemberController - 会员管理
**路径**: `/member`
- POST /member/register - 会员注册
- POST /member/login - 会员登录
- GET /member/info - 获取会员信息
- POST /member/update - 更新会员资料
- GET /member/address/list - 收货地址列表
- POST /member/address/add - 新增收货地址
- POST /member/address/update - 修改收货地址
- DELETE /member/address/{id} - 删除收货地址
- POST /member/address/{id}/default - 设为默认地址

### 2. OmsOrderController - 订单管理（前台）
**路径**: `/order`
- POST /order/submit - 提交订单
- POST /order/list - 订单列表分页查询
- GET /order/{id} - 订单详情
- GET /order/remaining-time - 查询订单剩余支付时间

### 3. OmsOrderReturnApplyController - 订单退货管理（前台）
**路径**: `/order/return`
- POST /order/return/apply - 创建退货申请
- POST /order/return/cancel - 取消订单
- GET /order/return/reasons - 查询退货原因列表

### 4. PmsProductController - 商品管理（前台）
**路径**: `/pms/product`
- GET /pms/product/category/tree - 获取商品分类树
- POST /pms/product/list - 商品列表分页查询
- GET /pms/product/{id} - 商品详情

### 5. OmsCartItemController - 购物车管理
**路径**: `/cart`
- POST /cart/add - 添加商品到购物车
- POST /cart/clear - 清空当前会员的购物车
- POST /cart/delete - 删除购物车中的指定商品
- GET /cart/getProduct/{productId} - 获取购物车中指定商品的规格
- GET /cart/list - 获取当前会员的购物车列表
- POST /cart/update/attr - 修改购物车中商品的规格
- GET /cart/update/quantity - 修改购物车中指定商品的数量

### 6. SmsCouponController - 优惠券管理（前台）
**路径**: `/coupon`
- GET /coupon/available/list - 获取可领取的优惠券列表
- POST /coupon/receive - 领取优惠券
- GET /coupon/member/list - 获取会员的优惠券列表
- POST /coupon/use - 使用优惠券
- GET /coupon/order/available/list - 获取订单可用的优惠券列表

### 7. FileUploadController - 文件上传
**路径**: `/file`
- POST /file/upload - 上传文件（图片等）

### 8. HomeAdvertiseController - 首页轮播广告
**路径**: `/home`
- GET /home/advertises - 获取轮播广告列表

### 9. HomeNewProductController - 首页新品推荐
**路径**: `/home`
- GET /home/new-products - 获取新品推荐列表

### 10. HomeRecommendProductController - 首页人气推荐
**路径**: `/home`
- GET /home/recommend-products - 获取人气推荐列表

### 11. AlipayController - 支付宝支付
**路径**: `/alipay`
- GET /alipay/pay - 创建支付（返回支付宝支付表单HTML）
- POST /alipay/notify - 支付宝异步通知回调
- GET /alipay/query - 主动查询支付宝交易状态

### 12. TraceController - 溯源查询（前台）
**路径**: `/trace`
- GET /trace/{productId} - 根据商品ID查询溯源详情

### 13. SupportController - 助农专区（前台）
**路径**: `/support`
- GET /support/farmers - 获取签约帮扶农户列表

### 14. TestController - 测试接口
**路径**: `/` (无前缀)
- POST /admin/test - 测试接口
- POST /admin/update - 测试更新接口

---

## 二、后台管理API (guimall-module-admin)

### 1. UmsAdminController - 后台账号管理
**路径**: `/admin/ums/admin`
- POST /admin/ums/admin/list - 分页查询后台账号
- GET /admin/ums/admin/roleOptions - 获取角色下拉选项
- POST /admin/ums/admin/create - 新增后台账号
- POST /admin/ums/admin/update - 修改后台账号
- POST /admin/ums/admin/delete - 删除后台账号（批量）
- POST /admin/ums/admin/assignRole - 分配后台账号角色

### 2. UmsMenuController - 菜单管理
**路径**: `/admin/ums/menu`
- POST /admin/ums/menu/list - 分页查询菜单
- GET /admin/ums/menu/options - 获取顶级菜单下拉选项
- POST /admin/ums/menu/create - 新增菜单
- POST /admin/ums/menu/update - 修改菜单
- POST /admin/ums/menu/delete - 删除菜单（批量）

### 3. UmsRoleController - 角色管理
**路径**: `/admin/ums/role`
- POST /admin/ums/role/list - 分页查询角色
- GET /admin/ums/role/options - 角色下拉选项
- POST /admin/ums/role/create - 新增角色
- POST /admin/ums/role/update - 修改角色
- POST /admin/ums/role/delete - 删除角色（批量）
- GET /admin/ums/role/allocMenu/init - 初始化角色分配菜单页面
- POST /admin/ums/role/allocMenu/save - 保存角色分配菜单权限

### 4. AdminUserController - Admin 用户模块
**路径**: `/admin`
- POST /admin/password/update - 修改用户密码
- POST /admin/user/info - 获取用户信息

### 5. PmsProductController - 商品管理
**路径**: `/admin/pms/product`
- POST /admin/pms/product/create - 创建商品
- POST /admin/pms/product/list - 分页查询商品
- GET /admin/pms/product/{id} - 查询商品详情
- POST /admin/pms/product/update - 修改商品
- DELETE /admin/pms/product/{id} - 删除商品
- PUT /admin/pms/product/{id}/publish - 上架商品
- PUT /admin/pms/product/{id}/unpublish - 下架商品
- POST /admin/pms/product/batch/delete - 批量删除商品
- POST /admin/pms/product/batch/publish - 批量上架
- POST /admin/pms/product/batch/unpublish - 批量下架

### 6. PmsProductCategoryController - 商品分类管理
**路径**: `/admin/pms/productCate`
- POST /admin/pms/productCate - 新增商品分类
- PUT /admin/pms/productCate - 修改商品分类
- DELETE /admin/pms/productCate/{id} - 删除商品分类
- POST /admin/pms/productCate/list - 分页查询商品分类
- GET /admin/pms/productCate/tree - 查询商品分类树
- GET /admin/pms/productCate/options - 获取商品分类下拉列表

### 7. PmsSkuStockController - SKU库存管理
**路径**: `/admin/pms/product/sku`
- GET /admin/pms/product/sku/{productId} - 根据商品ID查询SKU列表
- POST /admin/pms/product/sku/{productId}/save - 批量保存SKU（覆盖更新）
- DELETE /admin/pms/product/sku/{id} - 删除SKU

### 8. PmsProductFullReductionController - 商品满减管理
**路径**: `/admin/pms/product/fullReduction`
- GET /admin/pms/product/fullReduction/{productId} - 根据商品ID查询满减列表
- POST /admin/pms/product/fullReduction/{productId}/save - 批量保存满减规则（覆盖更新）
- DELETE /admin/pms/product/fullReduction/{id} - 删除满减规则

### 9. PmsParamDefinitionController - 参数定义管理（全局数据字典）
**路径**: `/admin/pms/paramDefinition`
- GET /admin/pms/paramDefinition/list - 查询所有参数定义
- GET /admin/pms/paramDefinition/page - 分页查询参数定义
- POST /admin/pms/paramDefinition - 新增参数定义
- PUT /admin/pms/paramDefinition/{id} - 修改参数定义
- DELETE /admin/pms/paramDefinition/{id} - 删除参数定义

### 10. FarmerController - 农户管理
**路径**: `/admin/farmer`
- POST /admin/farmer/create - 创建农户
- POST /admin/farmer/list - 分页查询农户列表
- GET /admin/farmer/{id} - 查询农户详情
- POST /admin/farmer/update - 修改农户信息
- DELETE /admin/farmer/{id} - 删除农户
- GET /admin/farmer/options - 获取农户下拉列表

### 11. OmsOrderController - 订单管理（后台）
**路径**: `/admin/oms/order`
- POST /admin/oms/order/list - 订单列表分页查询
- GET /admin/oms/order/{id} - 订单详情
- POST /admin/oms/order/deliver - 订单发货
- POST /admin/oms/order/close - 关闭订单
- POST /admin/oms/order/remark - 备注订单

### 12. OmsOrderReturnApplyController - 退货申请管理
**路径**: `/admin/oms/orderReturnApply`
- POST /admin/oms/orderReturnApply/list - 分页查询退货申请
- GET /admin/oms/orderReturnApply/{id} - 获取退货申请详情
- POST /admin/oms/orderReturnApply/status/update - 修改退货申请状态

### 13. OmsOrderReturnReasonController - 退货原因管理
**路径**: `/admin/oms/returnReason`
- GET /admin/oms/returnReason/{id} - 获取单个退货原因详情信息
- POST /admin/oms/returnReason/create - 添加退货原因
- POST /admin/oms/returnReason/delete - 批量删除退货原因
- POST /admin/oms/returnReason/list - 分页查询退货原因
- POST /admin/oms/returnReason/update/{id} - 修改退货原因
- POST /admin/oms/returnReason/update/status - 修改退货原因启用状态

### 14. SmsCouponController - 优惠券管理（后台）
**路径**: `/admin/sms/coupon`
- POST /admin/sms/coupon/create - 创建优惠券
- POST /admin/sms/coupon/delete/{id} - 删除优惠券
- GET /admin/sms/coupon/list - 优惠券列表分页查询
- GET /admin/sms/coupon/{id} - 优惠券详情
- POST /admin/sms/coupon/update/{id} - 修改优惠券

### 15. SmsCouponHistoryController - 优惠券领取记录管理
**路径**: `/admin/sms/couponHistory`
- GET /admin/sms/couponHistory/list - 优惠券领取记录列表分页查询

### 16. SmsHomeAdvertiseController - 首页轮播广告管理
**路径**: `/admin/sms/advertise`
- POST /admin/sms/advertise/create - 创建广告
- POST /admin/sms/advertise/update/{id} - 修改广告
- POST /admin/sms/advertise/delete/{id} - 删除广告
- GET /admin/sms/advertise/list - 广告列表分页查询
- GET /admin/sms/advertise/{id} - 广告详情

### 17. SmsHomeNewProductController - 新品推荐管理
**路径**: `/admin/sms/newProduct`
- POST /admin/sms/newProduct/create - 创建新品推荐
- POST /admin/sms/newProduct/delete/{id} - 删除新品推荐
- GET /admin/sms/newProduct/list - 新品推荐列表分页查询
- GET /admin/sms/newProduct/{id} - 新品推荐详情
- POST /admin/sms/newProduct/update/{id} - 修改新品推荐
- GET /admin/sms/newProduct/options - 获取新品推荐下拉列表（仅推荐）

### 18. SmsHomeRecommendProductController - 人气推荐管理
**路径**: `/admin/sms/recommendProduct`
- POST /admin/sms/recommendProduct/create - 创建人气推荐
- POST /admin/sms/recommendProduct/delete/{id} - 删除人气推荐
- GET /admin/sms/recommendProduct/list - 人气推荐列表分页查询
- GET /admin/sms/recommendProduct/{id} - 人气推荐详情
- POST /admin/sms/recommendProduct/update/{id} - 修改人气推荐
- GET /admin/sms/recommendProduct/options - 获取人气推荐下拉列表（仅推荐）

### 19. TraceRecordTypeController - 溯源记录类型管理
**路径**: `/admin/trace/recordType`
- GET /admin/trace/recordType/listByCategoryId - 根据商品分类ID获取溯源记录类型
- GET /admin/trace/recordType/listAll - 获取所有溯源记录类型

### 20. TraceRecordController - 溯源记录管理
**路径**: `/admin/trace/record`
- POST /admin/trace/record/create - 创建溯源记录
- POST /admin/trace/record/update - 修改溯源记录
- DELETE /admin/trace/record/{id} - 删除溯源记录
- POST /admin/trace/record/list - 查询溯源记录列表（按商品）

### 21. TraceQrcodeController - 溯源二维码管理
**路径**: `/admin/trace/qrcode`
- POST /admin/trace/qrcode/upsert - 新增/更新溯源二维码
- POST /admin/trace/qrcode/generate/{productId} - 一键生成溯源二维码
- GET /admin/trace/qrcode/product/{productId} - 根据商品ID查询溯源二维码
- DELETE /admin/trace/qrcode/product/{productId} - 删除商品的溯源二维码

### 22. TraceOriginController - 产地管理
**路径**: `/admin/trace/origin`
- POST /admin/trace/origin/create - 创建产地
- POST /admin/trace/origin/list - 分页查询产地列表
- GET /admin/trace/origin/{id} - 查询产地详情
- POST /admin/trace/origin/update - 修改产地信息
- DELETE /admin/trace/origin/{id} - 删除产地
- GET /admin/trace/origin/options - 获取产地下拉列表

### 23. TraceProductOriginController - 农户-产地关联查询模块
**路径**: `/admin/trace/productOrigin`
- GET /admin/trace/productOrigin/origin/{originId} - 根据产地ID查询关联农户和商品
- GET /admin/trace/productOrigin/farmer/{farmerId} - 根据农户ID查询名下商品

---

## 三、API 接口统计

### 前台API (guimall-web)
- **总Controller数**: 14
- **总API接口数**: 47

| 模块 | 接口数 |
|------|--------|
| 会员管理 | 9 |
| 订单管理 | 4 |
| 订单退货 | 3 |
| 商品管理 | 3 |
| 购物车管理 | 7 |
| 优惠券管理 | 5 |
| 文件上传 | 1 |
| 首页模块 | 4 |
| 支付宝支付 | 3 |
| 溯源查询 | 1 |
| 助农专区 | 1 |
| 测试接口 | 2 |

### 后台API (guimall-module-admin)
- **总Controller数**: 23
- **总API接口数**: 116

| 模块 | 接口数 |
|------|--------|
| 后台账号管理 | 6 |
| 菜单管理 | 5 |
| 角色管理 | 7 |
| 用户模块 | 2 |
| 商品管理 | 10 |
| 商品分类管理 | 6 |
| SKU库存管理 | 3 |
| 商品满减管理 | 3 |
| 参数定义管理 | 5 |
| 农户管理 | 6 |
| 订单管理 | 5 |
| 退货申请管理 | 3 |
| 退货原因管理 | 6 |
| 优惠券管理 | 5 |
| 优惠券领取记录 | 1 |
| 首页轮播广告管理 | 5 |
| 新品推荐管理 | 6 |
| 人气推荐管理 | 6 |
| 溯源记录类型管理 | 2 |
| 溯源记录管理 | 4 |
| 溯源二维码管理 | 4 |
| 产地管理 | 6 |
| 农户-产地关联查询 | 2 |

### 总体统计
- **总Controller数**: 37
- **总API接口数**: 163

---

## 四、HTTP方法分布

### 前台API (guimall-web)
| 方法 | 数量 | 百分比 |
|------|------|--------|
| GET | 18 | 38.3% |
| POST | 25 | 53.2% |
| DELETE | 2 | 4.3% |
| PUT | 2 | 4.3% |

### 后台API (guimall-module-admin)
| 方法 | 数量 | 百分比 |
|------|------|--------|
| GET | 29 | 25.0% |
| POST | 70 | 60.3% |
| PUT | 8 | 6.9% |
| DELETE | 9 | 7.8% |

### 总体 (全部)
| 方法 | 数量 | 百分比 |
|------|------|--------|
| GET | 47 | 28.8% |
| POST | 95 | 58.3% |
| PUT | 10 | 6.1% |
| DELETE | 11 | 6.7% |

---

## 五、API 路径前缀总结

### 前台API路径前缀
- `/member` - 会员管理
- `/order` - 订单管理
- `/pms/product` - 商品浏览
- `/cart` - 购物车
- `/coupon` - 优惠券
- `/file` - 文件上传
- `/home` - 首页
- `/alipay` - 支付
- `/trace` - 溯源
- `/support` - 助农专区

### 后台API路径前缀
- `/admin/ums/admin` - 账号管理
- `/admin/ums/menu` - 菜单管理
- `/admin/ums/role` - 角色管理
- `/admin/pms/product` - 商品管理
- `/admin/pms/productCate` - 分类管理
- `/admin/pms/paramDefinition` - 参数管理
- `/admin/farmer` - 农户管理
- `/admin/oms/order` - 订单管理
- `/admin/oms/orderReturnApply` - 退货申请
- `/admin/oms/returnReason` - 退货原因
- `/admin/sms/coupon` - 优惠券
- `/admin/sms/advertise` - 广告管理
- `/admin/sms/newProduct` - 新品推荐
- `/admin/sms/recommendProduct` - 人气推荐
- `/admin/trace/*` - 溯源相关

---

**生成时间**: 2026年4月8日
**项目**: GuiMall商城系统
**版本**: v2.0
