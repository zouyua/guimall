import axios from "@/axios";

/**
 * 后台账号管理（`UmsAdminController` → `/admin/ums/admin`）
 */

/** 分页查询后台账号 */
export function fetchUmsAdminList(data) {
  return axios.post("/admin/ums/admin/list", data);
}

/** 获取角色下拉选项（用于“新增/编辑/分配角色”） */
export function fetchUmsRoleOptions() {
  return axios.get("/admin/ums/admin/roleOptions");
}

/** 新增后台账号 */
export function createUmsAdmin(data) {
  return axios.post("/admin/ums/admin/create", data);
}

/** 修改后台账号 */
export function updateUmsAdmin(data) {
  return axios.post("/admin/ums/admin/update", data);
}

/** 批量删除后台账号（请求体为 ID 数组） */
export function deleteUmsAdmins(ids) {
  return axios.post("/admin/ums/admin/delete", ids);
}

/** 分配后台账号角色 */
export function assignUmsAdminRole(data) {
  return axios.post("/admin/ums/admin/assignRole", data);
}

