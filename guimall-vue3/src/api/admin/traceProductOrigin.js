import axios from "@/axios";

/**
 * 农户-产地关联查询（用于删除拦截）
 * 产地绑定已移至农户管理（创建/编辑农户时选择产地）
 */

/**
 * 根据产地 ID 查询关联的农户和商品（用于删除产地前拦截）
 */
export function getProductBindingsByOriginId(originId) {
  return axios.get(`/admin/trace/productOrigin/origin/${originId}`);
}

/**
 * 根据农户 ID 查询名下商品列表（用于删除农户前拦截）
 */
export function getProductBindingsByFarmerId(farmerId) {
  return axios.get(`/admin/trace/productOrigin/farmer/${farmerId}`);
}
