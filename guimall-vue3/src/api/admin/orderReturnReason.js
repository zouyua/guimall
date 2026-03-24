import axios from "@/axios";

/**
 * 退货原因（`OmsOrderReturnReasonController` → `/admin/oms/returnReason`）
 */

export function getOrderReturnReason(id) {
  return axios.get(`/admin/oms/returnReason/${id}`);
}

export function createOrderReturnReason(data) {
  return axios.post("/admin/oms/returnReason/create", data);
}

/** 批量删除，请求体为 ID 数组 */
export function deleteOrderReturnReasons(ids) {
  return axios.post("/admin/oms/returnReason/delete", ids);
}

export function fetchOrderReturnReasonList(data) {
  return axios.post("/admin/oms/returnReason/list", data);
}

export function updateOrderReturnReason(id, data) {
  return axios.post(`/admin/oms/returnReason/update/${id}`, data);
}

export function updateOrderReturnReasonStatus(data) {
  return axios.post("/admin/oms/returnReason/update/status", data);
}
