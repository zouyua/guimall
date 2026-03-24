import axios from "@/axios";

/**
 * 退货申请（`OmsOrderReturnApplyController` → `/admin/oms/orderReturnApply`）
 */

export function fetchOrderReturnApplyList(data) {
  return axios.post("/admin/oms/orderReturnApply/list", data);
}

export function getOrderReturnApplyDetail(id) {
  return axios.get(`/admin/oms/orderReturnApply/${id}`);
}

/** 审核/更新退货状态 */
export function updateOrderReturnApplyStatus(data) {
  return axios.post("/admin/oms/orderReturnApply/status/update", data);
}
