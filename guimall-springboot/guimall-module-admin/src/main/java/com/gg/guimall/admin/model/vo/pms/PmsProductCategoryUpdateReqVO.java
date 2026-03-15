package com.gg.guimall.admin.model.vo.pms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/12
 * @description: 修改商品分类请求 VO
 *
 * 用于后台修改商品分类信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "修改商品分类请求 VO")
public class PmsProductCategoryUpdateReqVO {

    /**
     * 分类ID
     */
    @ApiModelProperty(value = "分类ID")
    @NotNull(message = "分类ID不能为空")
    private Long id;

    /**
     * 父分类ID
     */
    @ApiModelProperty(value = "父分类ID")
    private Long parentId;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称")
    private String name;

    /**
     * 分类级别
     */
    @ApiModelProperty(value = "分类级别")
    private Integer level;

    /**
     * 商品单位
     */
    @ApiModelProperty(value = "商品单位")
    private String productUnit;

    /**
     * 导航栏显示状态
     */
    @ApiModelProperty(value = "导航栏显示状态")
    private Integer navStatus;

    /**
     * 显示状态
     */
    @ApiModelProperty(value = "显示状态")
    private Integer showStatus;

    /**
     * 排序
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