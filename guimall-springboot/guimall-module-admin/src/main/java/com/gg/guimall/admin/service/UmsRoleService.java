package com.gg.guimall.admin.service;

import com.gg.guimall.admin.model.vo.ums.role.*;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;

import java.util.List;

/**
 * 角色管理 Service
 */
public interface UmsRoleService {

    PageResponse<FindUmsRolePageListRspVO> findRolePageList(FindUmsRolePageListReqVO reqVO);

    Response createRole(CreateUmsRoleReqVO reqVO);

    Response updateRole(UpdateUmsRoleReqVO reqVO);

    Response deleteRoles(List<Long> ids);

    /**
     * 角色下拉选项（用于分配角色）
     */
    List<RoleOptionRspVO> findRoleOptions();

    AllocMenuInitRspVO initAllocMenu(Long roleId);

    Response saveAllocMenu(SaveUmsRoleAllocMenuReqVO reqVO);
}

