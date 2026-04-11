import axios from "@/axios";

/**
 * 管理端 - 会员积分管理 API
 */

/** 会员积分列表（分页 + 搜索） */
export function fetchMemberIntegrationList(params) {
  return axios.get("/admin/ums/integration/memberList", { params });
}

/** 查看指定会员的积分变动历史 */
export function fetchIntegrationHistory(params) {
  return axios.get("/admin/ums/integration/history", { params });
}

/** 管理员手动调整积分 */
export function adjustIntegration(data) {
  return axios.post("/admin/ums/integration/adjust", data);
}
