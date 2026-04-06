package com.gg.guimall.admin.model.vo.pms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品分页列表项响应 VO（含关联展示：分类名、农户名）
 *
 * @author wly
 * @url www.gg.com
 * @date 2026/3/15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "商品分页列表项响应 VO")
public class FindPmsProductPageListRspVO {

    @ApiModelProperty(value = "商品ID")
    private Long id;

    @ApiModelProperty(value = "商品分类ID")
    private Long productCategoryId;

    @ApiModelProperty(value = "商品分类名称（关联展示）")
    private String categoryName;

    @ApiModelProperty(value = "农户ID")
    private Long farmerId;

    @ApiModelProperty(value = "农户名称（关联展示）")
    private String farmerName;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "副标题")
    private String subTitle;

    @ApiModelProperty(value = "商品货号")
    private String productSn;

    @ApiModelProperty(value = "商品主图")
    private String pic;

    @ApiModelProperty(value = "销售价格")
    private BigDecimal price;

    @ApiModelProperty(value = "库存")
    private Integer stock;

    @ApiModelProperty(value = "上架状态：0下架，1上架")
    private Integer publishStatus;

    @ApiModelProperty(value = "销量")
    private Integer sale;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}
