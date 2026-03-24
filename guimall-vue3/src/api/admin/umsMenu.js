import axios from "@/axios";

/**
 * 菜单管理（`UmsMenuController` → `/admin/ums/menu`）
 */

/** 分页查询菜单 */
export function fetchUmsMenuList(data) {
  return axios.post("/admin/ums/menu/list", data);
}

/** 顶级菜单下拉选项 */
export function fetchUmsMenuOptions() {
  return axios.get("/admin/ums/menu/options");
}

/** 新增菜单 */
export function createUmsMenu(data) {
  return axios.post("/admin/ums/menu/create", data);
}

/** 修改菜单 */
export function updateUmsMenu(data) {
  return axios.post("/admin/ums/menu/update", data);
}

/** 批量删除菜单（请求体为 ID 数组） */
export function deleteUmsMenus(ids) {
  return axios.post("/admin/ums/menu/delete", ids);
}

