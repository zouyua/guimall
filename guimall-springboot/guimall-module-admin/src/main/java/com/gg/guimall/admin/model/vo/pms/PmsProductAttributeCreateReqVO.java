package com.gg.guimall.admin.model.vo.pms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/13
 * @description: 创建商品属性请求 VO
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "创建商品属性请求 VO")
public class PmsProductAttributeCreateReqVO {

    @ApiModelProperty(value = "属性分类ID")
    @NotNull(message = "属性分类ID不能为空")
    private Long productAttributeCategoryId;

    @ApiModelProperty(value = "属性名称")
    @NotBlank(message = "属性名称不能为空")
    private String name;

    @ApiModelProperty(value = "选择类型：0唯一；1单选；2多选")
    private Integer selectType;

    @ApiModelProperty(value = "输入类型：0手动；1列表")
    private Integer inputType;

    @ApiModelProperty(value = "可选值列表（逗号分隔）")
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
    @NotNull(message = "属性类型不能为空")
    private Integer type;
}
