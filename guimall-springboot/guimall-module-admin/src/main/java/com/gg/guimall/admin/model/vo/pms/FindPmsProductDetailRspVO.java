package com.gg.guimall.admin.model.vo.pms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/*@author:wgg
 * @url:www.gg.com
 * @date:2026/3/10
 * @description:商品详细
 * */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "查询商品详情响应 VO")
public class FindPmsProductDetailRspVO {

    @ApiModelProperty(value = "商品ID")
    private Long id;

    @ApiModelProperty(value = "商品分类ID")
    private Long productCategoryId;

    @ApiModelProperty(value = "商品属性分类ID")
    private Long productAttributeCategoryId;

    @ApiModelProperty(value = "农户ID")
    private Long farmerId;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "副标题")
    private String subTitle;

    @ApiModelProperty(value = "商品货号")
    private String productSn;

    @ApiModelProperty(value = "商品主图")
    private String pic;

    @ApiModelProperty(value = "商品相册图片（逗号分割）")
    private String albumPics;

    @ApiModelProperty(value = "商品描述")
    private String description;

    @ApiModelProperty(value = "销售价格")
    private BigDecimal price;

    @ApiModelProperty(value = "原价")
    private BigDecimal originalPrice;

    @ApiModelProperty(value = "促销价格")
    private BigDecimal promotionPrice;

    @ApiModelProperty(value = "库存")
    private Integer stock;

    @ApiModelProperty(value = "库存预警值")
    private Integer lowStock;

    @ApiModelProperty(value = "单位（斤/箱/袋）")
    private String unit;

    @ApiModelProperty(value = "重量（克）")
    private BigDecimal weight;

    @ApiModelProperty(value = "上架状态：0下架，1上架")
    private Integer publishStatus;

    @ApiModelProperty(value = "审核状态：0未审核，1通过")
    private Integer verifyStatus;

    @ApiModelProperty(value = "销量")
    private Integer sale;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "关键词")
    private String keywords;

    @ApiModelProperty(value = "备注")
    private String note;

    @ApiModelProperty(value = "详情标题")
    private String detailTitle;

    @ApiModelProperty(value = "详情描述")
    private String detailDesc;

    @ApiModelProperty(value = "产品详情网页内容")
    private String detailHtml;

    @ApiModelProperty(value = "促销类型：0没有促销使用原价；1使用满减价格；2使用会员价")
    private Integer promotionType;

    @ApiModelProperty(value = "促销开始时间")
    private LocalDateTime promotionStartTime;

    @ApiModelProperty(value = "促销结束时间")
    private LocalDateTime promotionEndTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}
