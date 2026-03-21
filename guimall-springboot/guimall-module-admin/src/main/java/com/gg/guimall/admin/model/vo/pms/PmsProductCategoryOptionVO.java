package com.gg.guimall.admin.model.vo.pms;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/20
 * @description: 商品分类下拉选项VO
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "商品分类下拉选项 VO")
public class PmsProductCategoryOptionVO {

    /**
     * 分类ID
     */
    private Long id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 父分类ID
     */
    private Long parentId;

    /**
     * 层级（用于前端缩进显示）
     */
    private Integer level;
}
