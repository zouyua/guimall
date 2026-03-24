import axios from "@/axios";

/**
 * 商品满减（`PmsProductFullReductionController` → `/admin/pms/product/fullReduction`）
 */

export function fetchFullReductionListByProductId(productId) {
  return axios.get(`/admin/pms/product/fullReduction/${productId}`);
}

export function saveFullReductionList(productId, fullReductionList) {
  return axios.post(`/admin/pms/product/fullReduction/${productId}/save`, fullReductionList);
}

export function deleteFullReduction(id) {
  return axios.delete(`/admin/pms/product/fullReduction/${id}`);
}
