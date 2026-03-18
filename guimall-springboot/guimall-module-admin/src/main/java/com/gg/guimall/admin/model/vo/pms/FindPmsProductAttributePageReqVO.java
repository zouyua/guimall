package com.gg.guimall.admin.model.vo.pms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/13
 * @description: 商品属性分页查询请求 VO
 **/
@Data
@ApiModel(value = "商品属性分页查询请求 VO")
public class FindPmsProductAttributePageReqVO {

    @ApiModelProperty(value = "当前页码")
    private Long current = 1L;

    @ApiModelProperty(value = "每页数量")
    private Long size = 10L;

    @ApiModelProperty(value = "属性分类ID")
    private Long productAttributeCategoryId;

    @ApiModelProperty(value = "属性名称（模糊搜索）")
    private String name;

    @ApiModelProperty(value = "属性类型：0规格；1参数")
    private Integer type;
}
