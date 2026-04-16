package com.gg.guimall.admin.service;

import com.gg.guimall.admin.model.vo.pms.PmsProductAttributeCategoryOptionVO;

import java.util.List;

/**
 * 商品属性分类 Service
 */
public interface PmsProductAttributeCategoryService {

    /**
     * 获取商品属性分类下拉选项
     */
    List<PmsProductAttributeCategoryOptionVO> findAttributeCategoryOptions();
}
