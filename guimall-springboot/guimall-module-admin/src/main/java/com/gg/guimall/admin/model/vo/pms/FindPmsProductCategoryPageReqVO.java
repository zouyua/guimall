package com.gg.guimall.admin.model.vo.pms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/12
 * @description: 商品分类分页查询请求 VO
 *
 * 用于商品分类分页查询，并支持模糊搜索
 */
@Data
@ApiModel(value = "商品分类分页查询请求 VO")
public class FindPmsProductCategoryPageReqVO {

    /**
     * 当前页码
     */
    @ApiModelProperty(value = "当前页码")
    private Integer pageNum = 1;

    /**
     * 每页显示数量
     */
    @ApiModelProperty(value = "每页数量")
    private Integer pageSize = 10;

    /**
     * 分类名称（用于模糊搜索）
     */
    @ApiModelProperty(value = "分类名称")
    private String name;

    /**
     * 启用状态：0禁用 1启用
     */
    @ApiModelProperty(value = "启用状态：0禁用 1启用")
    private Integer status;

}