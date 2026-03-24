import axios from "@/axios";

/**
 * 产地档案（`TraceOriginController` → `/admin/trace/origin`）
 */

export function createTraceOrigin(data) {
  return axios.post("/admin/trace/origin/create", data);
}

export function fetchTraceOriginList(data) {
  return axios.post("/admin/trace/origin/list", data);
}

export function getTraceOriginDetail(id) {
  return axios.get(`/admin/trace/origin/${id}`);
}

export function updateTraceOrigin(data) {
  return axios.post("/admin/trace/origin/update", data);
}

export function deleteTraceOrigin(id) {
  return axios.delete(`/admin/trace/origin/${id}`);
}

export function fetchTraceOriginOptions() {
  return axios.get("/admin/trace/origin/options");
}
