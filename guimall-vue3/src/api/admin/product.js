import axios from "@/axios";

/**
 * 商品 SPU 管理（`PmsProductController` → `/admin/pms/product`）
 */

export function createProduct(data) {
  return axios.post("/admin/pms/product/create", data);
}

/** 分页列表，请求体为 FindPmsProductPageListReqVO */
export function fetchProductList(data) {
  return axios.post("/admin/pms/product/list", data);
}

export function getProductDetail(id) {
  return axios.get(`/admin/pms/product/${id}`);
}

export function updateProduct(data) {
  return axios.post("/admin/pms/product/update", data);
}

export function deleteProduct(id) {
  return axios.delete(`/admin/pms/product/${id}`);
}

/** 上架 */
export function publishProduct(id) {
  return axios.put(`/admin/pms/product/${id}/publish`);
}

/** 下架 */
export function unpublishProduct(id) {
  return axios.put(`/admin/pms/product/${id}/unpublish`);
}

/** 批量删除 */
export function batchDeleteProducts(ids) {
  return axios.post("/admin/pms/product/batch/delete", ids);
}

/** 批量上架 */
export function batchPublishProducts(ids) {
  return axios.post("/admin/pms/product/batch/publish", ids);
}

/** 批量下架 */
export function batchUnpublishProducts(ids) {
  return axios.post("/admin/pms/product/batch/unpublish", ids);
}
