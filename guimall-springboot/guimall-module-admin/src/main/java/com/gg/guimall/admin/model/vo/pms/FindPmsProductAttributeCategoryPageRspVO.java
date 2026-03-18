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
 * @description: 商品属性分类分页查询响应 VO
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "商品属性分类分页查询响应 VO")
public class FindPmsProductAttributeCategoryPageRspVO {

    @ApiModelProperty(value = "属性分类ID")
    private Long id;

    @ApiModelProperty(value = "属性分类名称")
    private String name;

    @ApiModelProperty(value = "属性数量（规格数量）")
    private Integer attributeCount;

    @ApiModelProperty(value = "参数数量")
    private Integer paramCount;
}
