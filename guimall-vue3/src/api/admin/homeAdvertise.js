import axios from "@/axios";

/**
 * 首页轮播（`SmsHomeAdvertiseController` → `/admin/sms/advertise`）
 */

export function createHomeAdvertise(data) {
  return axios.post("/admin/sms/advertise/create", data);
}

export function updateHomeAdvertise(id, data) {
  return axios.post(`/admin/sms/advertise/update/${id}`, data);
}

export function deleteHomeAdvertise(id) {
  return axios.post(`/admin/sms/advertise/delete/${id}`);
}

export function fetchHomeAdvertiseList(params) {
  return axios.get("/admin/sms/advertise/list", { params });
}

export function getHomeAdvertiseDetail(id) {
  return axios.get(`/admin/sms/advertise/${id}`);
}
