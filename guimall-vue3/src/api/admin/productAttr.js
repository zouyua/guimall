import axios from "@/axios";

/**
 * 商品具体属性项（颜色、尺码等）
 * `PmsProductAttributeController` → `/admin/pms/productAttr/productAttrList`
 */

export function createProductAttr(data) {
  return axios.post("/admin/pms/productAttr/productAttrList/create", data);
}

export function fetchProductAttrList(data) {
  return axios.post("/admin/pms/productAttr/productAttrList/list", data);
}

export function updateProductAttr(data) {
  return axios.post("/admin/pms/productAttr/productAttrList/update", data);
}

export function deleteProductAttr(id) {
  return axios.delete(`/admin/pms/productAttr/productAttrList/${id}`);
}

/**
 * 按属性分类 ID 查询属性列表
 * @param {number} categoryId 属性分类 ID
 * @param {number} [type] 可选，后端按类型过滤
 */
export function fetchProductAttrByCategoryId(categoryId, type) {
  return axios.get(`/admin/pms/productAttr/productAttrList/category/${categoryId}`, {
    params: type != null ? { type } : {}
  });
}
