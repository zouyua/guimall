import axios from "@/axios";

/**
 * 溯源记录类型 API
 */

// 根据商品分类ID获取溯源记录类型
export function getRecordTypesByCategoryId(categoryId) {
  return axios.get("/admin/trace/recordType/listByCategoryId", {
    params: { categoryId }
  });
}

// 获取所有溯源记录类型
export function getAllRecordTypes() {
  return axios.get("/admin/trace/recordType/listAll");
}
