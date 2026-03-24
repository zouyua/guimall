import axios from "@/axios";

/**
 * 商品与产地绑定（`TraceProductOriginController` → `/admin/trace/productOrigin`）
 */

export function bindProductOrigin(data) {
  return axios.post("/admin/trace/productOrigin/bind", data);
}

export function getProductOriginByProductId(productId) {
  return axios.get(`/admin/trace/productOrigin/product/${productId}`);
}

/**
 * 根据产地 ID 查询绑定商品列表（用于删除前拦截）
 */
export function getProductBindingsByOriginId(originId) {
  return axios.get(`/admin/trace/productOrigin/origin/${originId}`);
}

/**
 * 根据农户 ID 查询绑定商品列表（用于删除农户前拦截）
 */
export function getProductBindingsByFarmerId(farmerId) {
  return axios.get(`/admin/trace/productOrigin/farmer/${farmerId}`);
}

export function unbindProductOrigin(productId) {
  return axios.delete(`/admin/trace/productOrigin/product/${productId}`);
}
