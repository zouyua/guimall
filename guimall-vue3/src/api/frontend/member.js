import axios from "@/axios";

/**
 * 前台会员 API（草稿）
 * 说明：
 * 1) 当前项目暂未实现 web 端会员 Controller，这里先提供接口设计草稿。
 * 2) 路径建议独立于后台 `/admin/**`，供用户端 H5/APP 调用。
 */

/** 会员注册 */
export function register(data) {
  return axios.post("/member/register", data);
}

/** 会员登录 */
export function login(data) {
  return axios.post("/member/login", data);
}

/** 当前登录会员信息 */
export function getMemberInfo() {
  return axios.get("/member/me");
}

/** 更新当前会员资料 */
export function updateMemberInfo(data) {
  return axios.post("/member/me/update", data);
}

/** 修改当前会员密码 */
export function updateMemberPassword(data) {
  return axios.post("/member/me/password/update", data);
}

/** 当前会员地址列表 */
export function getAddressList() {
  return axios.get("/member/address/list");
}

/** 新增地址 */
export function addAddress(data) {
  return axios.post("/member/address/create", data);
}

/** 修改地址 */
export function updateAddress(data) {
  return axios.post("/member/address/update", data);
}

/** 删除地址 */
export function deleteAddress(id) {
  return axios.delete(`/member/address/${id}`);
}

/** 设为默认地址 */
export function setDefaultAddress(id) {
  return axios.post(`/member/address/${id}/default`);
}

