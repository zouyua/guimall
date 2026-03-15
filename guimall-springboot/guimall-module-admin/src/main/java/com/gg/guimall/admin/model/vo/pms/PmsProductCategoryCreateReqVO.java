package com.gg.guimall.admin.model.vo.pms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/12
 * @description: 新增商品分类请求 VO
 *
 * 用于后台新增商品分类时接收前端参数
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "新增商品分类请求 VO")
public class PmsProductCategoryCreateReqVO {

    /**
     * 父分类ID
     * 0 表示一级分类
     */
    @ApiModelProperty(value = "父分类ID")
    private Long parentId;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称")
    @NotBlank(message = "分类名称不能为空")
    private String name;

    /**
     * 分类级别
     * 0 -> 一级分类
     * 1 -> 二级分类
     */
    @ApiModelProperty(value = "分类级别")
    private Integer level;

    /**
     * 商品单位
     * 例如：斤 / 箱 / 袋
     */
    @ApiModelProperty(value = "商品单位")
    private String productUnit;

    /**
     * 是否显示在导航栏
     * 0 -> 不显示
     * 1 -> 显示
     */
    @ApiModelProperty(value = "导航栏显示状态")
    private Integer navStatus;

    /**
     * 是否显示
     * 0 -> 不显示
     * 1 -> 显示
     */
    @ApiModelProperty(value = "显示状态")
    private Integer showStatus;

    /**
     * 排序
     * 数值越小越靠前
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    /**
     * 分类图标
     */
    @ApiModelProperty(value = "分类图标")
    private String icon;

    /**
     * 关键词
     */
    @ApiModelProperty(value = "关键词")
    private String keywords;

    /**
     * 分类描述
     */
    @ApiModelProperty(value = "分类描述")
    private String description;

}