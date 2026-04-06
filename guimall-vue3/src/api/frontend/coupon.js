import axios from "@/axios";

/**
 * 前台优惠券 API
 */

// 获取可领取的优惠券列表
export function getAvailableCouponList(memberId) {
  return axios.get("/coupon/available/list", { params: { memberId } });
}

// 领取优惠券
export function receiveCoupon(data) {
  return axios.post("/coupon/receive", data);
}

// 获取会员的优惠券列表
export function getMemberCouponList(memberId, useStatus) {
  return axios.get("/coupon/member/list", { params: { memberId, useStatus } });
}

// 获取订单可用的优惠券列表（结算页使用）
export function getOrderAvailableCoupons(memberId, totalAmount) {
  return axios.get("/coupon/order/available/list", { params: { memberId, totalAmount } });
}
