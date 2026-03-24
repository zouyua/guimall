import axios from "@/axios";

/**
 * 人气推荐（`SmsHomeRecommendProductController` → `/admin/sms/recommendProduct`）
 */

export function createHomeRecommendProduct(data) {
  return axios.post("/admin/sms/recommendProduct/create", data);
}

export function deleteHomeRecommendProduct(id) {
  return axios.post(`/admin/sms/recommendProduct/delete/${id}`);
}

export function fetchHomeRecommendProductList(params) {
  return axios.get("/admin/sms/recommendProduct/list", { params });
}

export function getHomeRecommendProductDetail(id) {
  return axios.get(`/admin/sms/recommendProduct/${id}`);
}

export function updateHomeRecommendProduct(id, data) {
  return axios.post(`/admin/sms/recommendProduct/update/${id}`, data);
}

export function fetchHomeRecommendProductOptions() {
  return axios.get("/admin/sms/recommendProduct/options");
}
