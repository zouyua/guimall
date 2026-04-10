import axios from "@/axios";

/**
 * 会员等级管理（`UmsMemberLevelController` → `/admin/ums/memberLevel`）
 */

/** 分页查询等级列表 */
export function fetchMemberLevelList(params) {
  return axios.get("/admin/ums/memberLevel/list", { params });
}

/** 所有启用的等级（下拉选择用） */
export function fetchAllEnabledLevels() {
  return axios.get("/admin/ums/memberLevel/allEnabled");
}

/** 新增等级 */
export function addMemberLevel(data) {
  return axios.post("/admin/ums/memberLevel/add", data);
}

/** 编辑等级 */
export function updateMemberLevel(data) {
  return axios.post("/admin/ums/memberLevel/update", data);
}

/** 删除等级 */
export function deleteMemberLevel(id) {
  return axios.post(`/admin/ums/memberLevel/delete/${id}`);
}

/** 调整会员等级 */
export function adjustMemberLevel(data) {
  return axios.post("/admin/ums/memberLevel/adjustMemberLevel", data);
}

/** 查看会员等级信息 */
export function getMemberLevelInfo(memberId) {
  return axios.get("/admin/ums/memberLevel/memberInfo", { params: { memberId } });
}
