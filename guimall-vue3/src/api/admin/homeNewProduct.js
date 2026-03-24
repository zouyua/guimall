import axios from "@/axios";

/**
 * 新品推荐（`SmsHomeNewProductController` → `/admin/sms/newProduct`）
 */

export function createHomeNewProduct(data) {
  return axios.post("/admin/sms/newProduct/create", data);
}

export function deleteHomeNewProduct(id) {
  return axios.post(`/admin/sms/newProduct/delete/${id}`);
}

export function fetchHomeNewProductList(params) {
  return axios.get("/admin/sms/newProduct/list", { params });
}

export function getHomeNewProductDetail(id) {
  return axios.get(`/admin/sms/newProduct/${id}`);
}

export function updateHomeNewProduct(id, data) {
  return axios.post(`/admin/sms/newProduct/update/${id}`, data);
}

export function fetchHomeNewProductOptions() {
  return axios.get("/admin/sms/newProduct/options");
}
