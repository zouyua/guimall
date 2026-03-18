package com.gg.guimall.admin.service;

import com.gg.guimall.admin.model.vo.pms.*;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/13
 * @description: 商品属性 Service
 **/
public interface PmsProductAttributeService {

    /**
     * 创建商品属性
     */
    Response createAttribute(PmsProductAttributeCreateReqVO reqVO);

    /**
     * 分页查询商品属性列表
     */
    PageResponse findAttributePageList(FindPmsProductAttributePageReqVO reqVO);

    /**
     * 修改商品属性
     */
    Response updateAttribute(PmsProductAttributeUpdateReqVO reqVO);

    /**
     * 删除商品属性
     */
    Response deleteAttribute(Long id);

    /**
     * 根据分类ID查询属性列表
     */
    Response findAttributeListByCategoryId(Long categoryId, Integer type);
}
