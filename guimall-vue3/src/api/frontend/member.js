import axios from "@/axios";

export function memberLogin(data) {
  return axios.post("/member/login", data);
}

export function memberRegister(data) {
  return axios.post("/member/register", data);
}

export function getMemberInfoApi(memberId) {
  return axios.get("/member/info", { params: { memberId } });
}

export function updateMemberInfo(data) {
  return axios.post("/member/update", data);
}

export function updateMemberPassword(data) {
  return axios.post("/member/updatePassword", data);
}

export function getAddressList(memberId) {
  return axios.get("/member/address/list", { params: { memberId } });
}

export function addAddress(data) {
  return axios.post("/member/address/add", data);
}

export function updateAddress(data) {
  return axios.post("/member/address/update", data);
}

export function deleteAddress(id) {
  return axios.delete(`/member/address/${id}`);
}

export function setDefaultAddress(id, memberId) {
  return axios.post(`/member/address/${id}/default`, null, { params: { memberId } });
}

/**
 * 积分变动历史（分页）
 */
export function getIntegrationHistory(params) {
  return axios.get("/member/integration/history", { params });
}

// ==================== 会员等级相关 ====================

/** 获取所有可用等级列表 */
export function getMemberLevelList() {
  return axios.get("/member/level/list");
}

/** 获取当前会员等级 */
export function getCurrentMemberLevel(memberId) {
  return axios.get("/member/level/current", { params: { memberId } });
}

/** 开通/升级会员等级 */
export function purchaseMemberLevel(memberId, levelId) {
  return axios.post("/member/level/purchase", null, { params: { memberId, levelId } });
}
