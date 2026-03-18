package com.gg.guimall.admin.model.vo.pms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/13
 * @description: 商品属性分类下拉选项 VO
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "商品属性分类下拉选项 VO")
public class PmsProductAttributeCategoryOptionVO {

    @ApiModelProperty(value = "属性分类ID")
    private Long id;

    @ApiModelProperty(value = "属性分类名称")
    private String name;
}
