import axios from "@/axios";

/**
 * 优惠券（后台）（`SmsCouponController` → `/admin/sms/coupon`）
 * 列表/详情为 GET + query/path，与 mall-admin 常见写法一致。
 */

export function createCoupon(data) {
  return axios.post("/admin/sms/coupon/create", data);
}

export function deleteCoupon(id) {
  return axios.post(`/admin/sms/coupon/delete/${id}`);
}

/** 分页列表，请求参数为 query（与后端 FindSmsCouponPageListReqVO 字段绑定） */
export function fetchCouponList(params) {
  return axios.get("/admin/sms/coupon/list", { params });
}

export function getCouponDetail(id) {
  return axios.get(`/admin/sms/coupon/${id}`);
}

export function updateCoupon(id, data) {
  return axios.post(`/admin/sms/coupon/update/${id}`, data);
}
