import axios from "@/axios";

/**
 * 商品 SKU 库存（`PmsSkuStockController` → `/admin/pms/product/sku`）
 */

export function fetchSkuListByProductId(productId) {
  return axios.get(`/admin/pms/product/sku/${productId}`);
}

/** 批量保存 SKU（覆盖式） */
export function saveSkuList(productId, skuList) {
  return axios.post(`/admin/pms/product/sku/${productId}/save`, skuList);
}

export function deleteSku(id) {
  return axios.delete(`/admin/pms/product/sku/${id}`);
}
