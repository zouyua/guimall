package com.gg.guimall.admin.service;

import com.gg.guimall.admin.model.vo.ums.admin.*;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;

import java.util.List;

/**
 * 后台账号管理 Service
 */
public interface UmsAdminService {

    PageResponse<FindUmsAdminPageListRspVO> findAdminPageList(FindUmsAdminPageListReqVO reqVO);

    Response createAdmin(CreateUmsAdminReqVO reqVO);

    Response updateAdmin(UpdateUmsAdminReqVO reqVO);

    Response deleteAdmins(List<Long> ids);

    Response assignRole(AssignUmsAdminRoleReqVO reqVO);
}

