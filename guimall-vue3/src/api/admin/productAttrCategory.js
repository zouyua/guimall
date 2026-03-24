import axios from "@/axios";

/**
 * 商品「属性分类」（规格/参数类型，非具体属性项）
 * `PmsProductAttributeCategoryController` → `/admin/pms/productAttr`
 */

export function createProductAttrCategory(data) {
  return axios.post("/admin/pms/productAttr/create", data);
}

export function fetchProductAttrCategoryList(data) {
  return axios.post("/admin/pms/productAttr/list", data);
}

export function updateProductAttrCategory(data) {
  return axios.post("/admin/pms/productAttr/update", data);
}

export function deleteProductAttrCategory(id) {
  return axios.delete(`/admin/pms/productAttr/${id}`);
}

export function fetchProductAttrCategoryOptions() {
  return axios.get("/admin/pms/productAttr/options");
}
