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
