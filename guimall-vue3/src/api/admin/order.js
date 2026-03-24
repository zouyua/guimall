import axios from "@/axios";

/**
 * 订单管理（`OmsOrderController` → `/admin/oms/order`）
 */

export function fetchOrderList(data) {
  return axios.post("/admin/oms/order/list", data);
}

export function getOrderDetail(id) {
  return axios.get(`/admin/oms/order/${id}`);
}

/** 发货 */
export function deliverOrder(data) {
  return axios.post("/admin/oms/order/deliver", data);
}

/** 关闭订单 */
export function closeOrder(data) {
  return axios.post("/admin/oms/order/close", data);
}

/** 备注 */
export function remarkOrder(data) {
  return axios.post("/admin/oms/order/remark", data);
}
