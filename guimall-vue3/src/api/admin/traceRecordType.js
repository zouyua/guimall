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

// 新增溯源记录类型
export function createRecordType(data) {
  return axios.post("/admin/trace/recordType/create", data);
}

// 更新溯源记录类型
export function updateRecordType(data) {
  return axios.post("/admin/trace/recordType/update", data);
}

// 删除溯源记录类型
export function deleteRecordType(id) {
  return axios.post("/admin/trace/recordType/delete", null, {
    params: { id }
  });
}
