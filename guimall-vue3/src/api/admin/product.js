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
