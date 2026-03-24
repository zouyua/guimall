import axios from "@/axios";

/**
 * 溯源记录（`TraceRecordController` → `/admin/trace/record`）
 */

export function createTraceRecord(data) {
  return axios.post("/admin/trace/record/create", data);
}

export function updateTraceRecord(data) {
  return axios.post("/admin/trace/record/update", data);
}

export function deleteTraceRecord(id) {
  return axios.delete(`/admin/trace/record/${id}`);
}

export function fetchTraceRecordList(data) {
  return axios.post("/admin/trace/record/list", data);
}
