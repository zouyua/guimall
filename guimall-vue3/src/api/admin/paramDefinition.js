import axios from "@/axios";

/**
 * 商品参数定义（模板，挂在分类下）
 * 对应后端 PmsParamDefinitionController
 */

/** 查询分类下所有参数定义 */
export function fetchParamDefinitions(categoryId) {
  return axios.get(`/admin/productCategory/${categoryId}/params`);
}

/** 添加参数定义 */
export function createParamDefinition(categoryId, data) {
  return axios.post(`/admin/productCategory/${categoryId}/params`, data);
}

/** 修改参数定义 */
export function updateParamDefinition(id, data) {
  return axios.put(`/admin/productCategory/params/${id}`, data);
}

/** 删除参数定义 */
export function deleteParamDefinition(id) {
  return axios.delete(`/admin/productCategory/params/${id}`);
}
