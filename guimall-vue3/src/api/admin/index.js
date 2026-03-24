/**
 * 后台管理 API 聚合导出（按需 import 亦可单独引用各文件，避免整包过大）
 *
 * 路径均相对于 axios baseURL（`/api`），与 `guimall-springboot` 中 `**/admin/controller` 一致。
 */

export * from "./user";
export * from "./product";
export * from "./productCategory";
export * from "./productAttrCategory";
export * from "./productAttr";
export * from "./productSku";
export * from "./productFullReduction";
export * from "./farmer";
export * from "./order";
export * from "./orderReturnApply";
export * from "./orderReturnReason";
export * from "./traceOrigin";
export * from "./traceProductOrigin";
export * from "./traceRecord";
export * from "./traceQrcode";
export * from "./coupon";
export * from "./couponHistory";
export * from "./homeAdvertise";
export * from "./homeNewProduct";
export * from "./homeRecommendProduct";

export * from "./umsMenu";
export * from "./umsRole";
export * from "./umsAdmin";
