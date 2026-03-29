package com.gg.guimall.admin.model.vo.pms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/*@author:wgg
 * @url:www.gg.com
 * @date:2026/3/10
 * @description:创建商品请求 VO
 * */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "创建商品请求 VO")
public class PmsProductCreateReqVO {

    @ApiModelProperty(value = "商品分类ID")
    @NotNull(message = "商品分类不能为空")
    private Long productCategoryId;

    @ApiModelProperty(value = "商品属性分类ID")
    private Long productAttributeCategoryId;

    @ApiModelProperty(value = "农户ID")
    @NotNull(message = "农户ID不能为空")
    private Long farmerId;

    @ApiModelProperty(value = "商品名称")
    @NotBlank(message = "商品名称不能为空")
    private String name;

    @ApiModelProperty(value = "副标题")
    private String subTitle;

    @ApiModelProperty(value = "商品货号")
    @NotBlank(message = "商品货号不能为空")
    private String productSn;

    @ApiModelProperty(value = "商品主图")
    private String pic;

    @ApiModelProperty(value = "商品相册图片（逗号分割）")
    private String albumPics;

    @ApiModelProperty(value = "商品描述")
    private String description;

    @ApiModelProperty(value = "销售价格")
    @NotNull(message = "商品价格不能为空")
    private BigDecimal price;

    @ApiModelProperty(value = "市场价/原价（划线价）")
    private BigDecimal marketPrice;

    @ApiModelProperty(value = "库存")
    @NotNull(message = "库存不能为空")
    private Integer stock;

    @ApiModelProperty(value = "单位（斤/箱/袋）")
    private String unit;

    @ApiModelProperty(value = "重量（克）")
    private BigDecimal weight;

    @ApiModelProperty(value = "上架状态")
    private Integer publishStatus;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "关键词")
    private String keywords;

    @ApiModelProperty(value = "备注")
    private String note;

    @ApiModelProperty(value = "是否新品推荐：0否 1是")
    private Integer isNew;

    @ApiModelProperty(value = "是否人气推荐：0否 1是")
    private Integer isRecommend;

    @ApiModelProperty(value = "商品详情HTML")
    private String detailHtml;

}