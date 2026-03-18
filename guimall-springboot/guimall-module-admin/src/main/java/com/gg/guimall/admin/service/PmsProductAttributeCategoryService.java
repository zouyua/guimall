package com.gg.guimall.admin.service;

import com.gg.guimall.admin.model.vo.pms.*;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/13
 * @description: 商品属性分类 Service
 **/
public interface PmsProductAttributeCategoryService {

    /**
     * 创建属性分类
     */
    Response createAttributeCategory(PmsProductAttributeCategoryCreateReqVO reqVO);

    /**
     * 分页查询属性分类列表
     */
    PageResponse findAttributeCategoryPageList(FindPmsProductAttributeCategoryPageReqVO reqVO);

    /**
     * 修改属性分类
     */
    Response updateAttributeCategory(PmsProductAttributeCategoryUpdateReqVO reqVO);

    /**
     * 删除属性分类
     */
    Response deleteAttributeCategory(Long id);

    /**
     * 获取属性分类下拉列表
     */
    Response findAttributeCategoryOptions();
}
