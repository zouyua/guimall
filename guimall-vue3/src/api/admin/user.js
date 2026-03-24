import axios from "@/axios";

/**
 * 登录与当前管理员信息（对应 `AdminUserController`、JWT `/login`）
 * axios 已配置 baseURL=`/api`，此处路径为后端上下文下的相对路径。
 */

/** 登录，成功后请将返回的 token 写入 Cookie（与现有 login 页逻辑一致） */
export function login(username, password) {
  return axios.post("/login", { username, password });
}

/** 获取当前登录管理员信息（需携带 Authorization） */
export function getUserInfo() {
  return axios.post("/admin/user/info");
}

/** 修改当前用户密码 */
export function updateAdminPassword(data) {
  return axios.post("/admin/password/update", data);
}
