import axios from "@/axios";

/**
 * 农户管理（`FarmerController` → `/admin/farmer`）
 */

export function createFarmer(data) {
  return axios.post("/admin/farmer/create", data);
}

export function fetchFarmerList(data) {
  return axios.post("/admin/farmer/list", data);
}

export function getFarmerDetail(id) {
  return axios.get(`/admin/farmer/${id}`);
}

export function updateFarmer(data) {
  return axios.post("/admin/farmer/update", data);
}

export function deleteFarmer(id) {
  return axios.delete(`/admin/farmer/${id}`);
}

export function fetchFarmerOptions() {
  return axios.get("/admin/farmer/options");
}
