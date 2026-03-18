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
 * @description: 商品属性分页查询响应 VO
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "商品属性分页查询响应 VO")
public class FindPmsProductAttributePageRspVO {

    @ApiModelProperty(value = "属性ID")
    private Long id;

    @ApiModelProperty(value = "属性分类ID")
    private Long productAttributeCategoryId;

    @ApiModelProperty(value = "属性名称")
    private String name;

    @ApiModelProperty(value = "选择类型：0唯一；1单选；2多选")
    private Integer selectType;

    @ApiModelProperty(value = "输入类型：0手动；1列表")
    private Integer inputType;

    @ApiModelProperty(value = "可选值列表")
    private String inputList;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "筛选类型：0不筛选；1普通筛选；2颜色筛选")
    private Integer filterType;

    @ApiModelProperty(value = "搜索类型：0不搜索；1关键字搜索；2范围搜索")
    private Integer searchType;

    @ApiModelProperty(value = "是否关联：0不关联；1关联")
    private Integer relatedStatus;

    @ApiModelProperty(value = "是否支持手动新增：0不支持；1支持")
    private Integer handAddStatus;

    @ApiModelProperty(value = "属性类型：0规格；1参数")
    private Integer type;
}
