import axios from "@/axios";

/**
 * 溯源二维码（`TraceQrcodeController` → `/admin/trace/qrcode`）
 */

export function upsertTraceQrcode(data) {
  return axios.post("/admin/trace/qrcode/upsert", data);
}

export function getTraceQrcodeByProductId(productId) {
  return axios.get(`/admin/trace/qrcode/product/${productId}`);
}

export function deleteTraceQrcodeByProductId(productId) {
  return axios.delete(`/admin/trace/qrcode/product/${productId}`);
}
