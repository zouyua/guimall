import axios from "@/axios";

/**
 * 会员管理（`UmsMemberController` → `/admin/ums/member`）
 */

/** 分页查询会员列表 */
export function fetchMemberList(params) {
  return axios.get("/admin/ums/member/list", { params });
}

/** 获取会员详情 */
export function getMemberDetail(id) {
  return axios.get(`/admin/ums/member/detail/${id}`);
}

/** 更新会员信息 */
export function updateMember(data) {
  return axios.post("/admin/ums/member/update", data);
}

/** 切换会员状态 */
export function toggleMemberStatus(id) {
  return axios.post(`/admin/ums/member/toggleStatus/${id}`);
}
