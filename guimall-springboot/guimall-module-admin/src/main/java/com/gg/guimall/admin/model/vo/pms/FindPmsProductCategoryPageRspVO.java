package com.gg.guimall.admin.model.vo.pms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/12
 * @description: 商品分类分页查询返回 VO
 *
 * 用于后台商品分类分页列表展示
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "商品分类分页返回 VO")
public class FindPmsProductCategoryPageRspVO {

    /**
     * 分类ID
     */
    @ApiModelProperty(value = "分类ID")
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
     * 启用状态：0禁用 1启用
     */
    @ApiModelProperty(value = "启用状态")
    private Integer status;

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

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

}