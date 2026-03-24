import axios from "@/axios";

/**
 * 商品分类（`PmsProductCategoryController` → `/admin/pms/productCate`）
 */

export function createProductCategory(data) {
  return axios.post("/admin/pms/productCate", data);
}

export function updateProductCategory(data) {
  return axios.put("/admin/pms/productCate", data);
}

export function deleteProductCategory(id) {
  return axios.delete(`/admin/pms/productCate/${id}`);
}

/** 分页列表 */
export function fetchProductCategoryList(data) {
  return axios.post("/admin/pms/productCate/list", data);
}

/** 分类树（用于级联、树形表格） */
export function fetchProductCategoryTree() {
  return axios.get("/admin/pms/productCate/tree");
}

/** 下拉选项 */
export function fetchProductCategoryOptions() {
  return axios.get("/admin/pms/productCate/options");
}
