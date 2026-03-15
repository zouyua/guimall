package com.gg.guimall.admin.model.vo.pms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/12
 * @description: 商品分类树结构返回 VO
 *
 * 用于前端展示商品分类树结构
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "商品分类树结构返回 VO")
public class FindPmsProductCategoryTreeRspVO {

    /**
     * 分类ID
     */
    @ApiModelProperty(value = "分类ID")
    private Long id;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称")
    private String name;

    /**
     * 父分类ID
     */
    @ApiModelProperty(value = "父分类ID")
    private Long parentId;

    /**
     * 分类级别
     */
    @ApiModelProperty(value = "分类级别")
    private Integer level;

    /**
     * 子分类
     *
     * 用于构建树结构
     */
    @ApiModelProperty(value = "子分类")
    private List<FindPmsProductCategoryTreeRspVO> children;

}