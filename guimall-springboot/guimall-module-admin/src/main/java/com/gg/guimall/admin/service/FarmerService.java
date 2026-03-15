package com.gg.guimall.admin.service;

import com.gg.guimall.admin.model.vo.farmer.*;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/12
 * @description: 农户管理 Service
 **/
public interface FarmerService {

    /**
     * 创建农户
     */
    Response createFarmer(CreateFarmerReqVO reqVO);

    /**
     * 分页查询农户列表
     */
    PageResponse findFarmerPageList(FindFarmerPageListReqVO reqVO);

    /**
     * 查询农户详情
     */
    Response findFarmerDetail(Long id);

    /**
     * 修改农户信息
     */
    Response updateFarmer(UpdateFarmerReqVO reqVO);

    /**
     * 删除农户
     */
    Response deleteFarmer(Long id);

    /**
     * 获取农户下拉列表（用于商品添加时选择）
     */
    Response findFarmerOptions();
}