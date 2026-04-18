package com.gg.guimall.admin.model.vo.pms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

    @ApiModelProperty(value = "商品相册图片（逗号分割）")
    private String albumPics;

    @ApiModelProperty(value = "商品相册图片列表")
    private List<String> albumPicList;

    @ApiModelProperty(value = "商品描述")
    private String description;

    @ApiModelProperty(value = "销售价格")
    private BigDecimal price;

    @ApiModelProperty(value = "市场价/原价（划线价）")
    private BigDecimal marketPrice;

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

    @ApiModelProperty(value = "是否新品推荐：0否 1是")
    private Integer isNew;

    @ApiModelProperty(value = "是否人气推荐：0否 1是")
    private Integer isRecommend;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "关键词")
    private String keywords;

    @ApiModelProperty(value = "备注")
    private String note;

    @ApiModelProperty(value = "产品详情网页内容")
    private String detailHtml;

    @ApiModelProperty(value = "商品参数列表")
    private List<ProductParamItemVO> productParams;

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
