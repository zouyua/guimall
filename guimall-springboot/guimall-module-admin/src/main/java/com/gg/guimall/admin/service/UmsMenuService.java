package com.gg.guimall.admin.service;

import com.gg.guimall.admin.model.vo.ums.menu.*;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;

import java.util.List;

/**
 * 菜单管理 Service
 */
public interface UmsMenuService {

    PageResponse<FindUmsMenuPageListRspVO> findMenuPageList(FindUmsMenuPageListReqVO reqVO);

    Response createMenu(CreateUmsMenuReqVO reqVO);

    Response updateMenu(UpdateUmsMenuReqVO reqVO);

    Response deleteMenus(List<Long> ids);

    List<MenuOptionRspVO> findMenuOptions();
}

