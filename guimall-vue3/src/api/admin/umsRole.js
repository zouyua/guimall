import axios from "@/axios";

/**
 * 角色管理（`UmsRoleController` → `/admin/ums/role`）
 */

/** 分页查询角色 */
export function fetchUmsRoleList(data) {
  return axios.post("/admin/ums/role/list", data);
}

/** 获取角色下拉选项 */
export function fetchUmsRoleOptions() {
  return axios.get("/admin/ums/role/options");
}

/** 新增角色 */
export function createUmsRole(data) {
  return axios.post("/admin/ums/role/create", data);
}

/** 修改角色 */
export function updateUmsRole(data) {
  return axios.post("/admin/ums/role/update", data);
}

/** 批量删除角色（请求体为 ID 数组） */
export function deleteUmsRoles(ids) {
  return axios.post("/admin/ums/role/delete", ids);
}

/** 初始化“分配菜单权限”页面（Response<AllocMenuInitRspVO>） */
export function initUmsRoleAllocMenu(roleId) {
  return axios.get("/admin/ums/role/allocMenu/init", { params: { roleId } });
}

/** 保存“分配菜单权限” */
export function saveUmsRoleAllocMenu(data) {
  return axios.post("/admin/ums/role/allocMenu/save", data);
}

