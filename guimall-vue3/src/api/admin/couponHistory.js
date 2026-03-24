import axios from "@/axios";

/**
 * 优惠券领取记录（`SmsCouponHistoryController` → `/admin/sms/couponHistory`）
 */

export function fetchCouponHistoryList(params) {
  return axios.get("/admin/sms/couponHistory/list", { params });
}
