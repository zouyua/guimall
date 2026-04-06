import axios from "@/axios";

/**
 * 商品参数定义（全局数据字典）
 * 对应后端 PmsParamDefinitionController
 */

/** 查询所有参数定义 */
export function fetchParamDefinitions(params) {
  // 如果传入了分页参数，使用分页接口
  if (params && (params.current || params.size)) {
    return axios.get("/admin/pms/paramDefinition/page", { params });
  }
  // 否则使用列表接口
  return axios.get("/admin/pms/paramDefinition/list");
}

/** 添加参数定义 */
export function createParamDefinition(data) {
  return axios.post("/admin/pms/paramDefinition", data);
}

/** 修改参数定义 */
export function updateParamDefinition(id, data) {
  return axios.put(`/admin/pms/paramDefinition/${id}`, data);
}

/** 删除参数定义 */
export function deleteParamDefinition(id) {
  return axios.delete(`/admin/pms/paramDefinition/${id}`);
}
