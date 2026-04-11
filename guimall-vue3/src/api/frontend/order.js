import axios from "@/axios";

// 提交订单
export function submitOrder(data) {
  return axios.post("/order/submit", data);
}

// 订单列表
export function getOrderList(data) {
  return axios.post("/order/list", data);
}

// 订单详情
export function getOrderDetail(id, memberId) {
  return axios.get(`/order/${id}`, { params: { memberId } });
}

// 发起支付宝支付（返回支付表单HTML）
export function createAlipay(orderSn) {
  return axios.get("/alipay/pay", { params: { orderSn } });
}

// 主动查询支付宝交易状态（同步回跳后调用）
export function queryAlipayStatus(orderSn) {
  return axios.get("/alipay/query", { params: { orderSn } });
}

// 查询订单剩余支付时间（秒）
export function getRemainingTime(orderSn) {
  return axios.get("/order/remaining-time", { params: { orderSn } });
}

// 确认收货（前台）
export function confirmReceipt(orderId, memberId) {
  return axios.post("/order/confirmReceipt", null, {
    params: { orderId, memberId }
  });
}
