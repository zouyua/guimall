package com.gg.guimall.admin.model.vo.pms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/18
 * @description: SKU库存 VO（新增/修改复用）
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "SKU库存 VO")
public class PmsSkuStockVO {

    @ApiModelProperty(value = "SKU ID（修改时必填）")
    private Long id;

    @ApiModelProperty(value = "商品ID")
    @NotNull(message = "商品ID不能为空")
    private Long productId;

    @ApiModelProperty(value = "SKU编码")
    @NotBlank(message = "SKU编码不能为空")
    private String skuCode;

    @ApiModelProperty(value = "销售价格")
    @NotNull(message = "销售价格不能为空")
    private BigDecimal price;

    @ApiModelProperty(value = "单品促销价格")
    private BigDecimal promotionPrice;

    @ApiModelProperty(value = "库存")
    private Integer stock;

    @ApiModelProperty(value = "预警库存")
    private Integer lowStock;

    @ApiModelProperty(value = "展示图片")
    private String pic;

    @ApiModelProperty(value = "销售属性JSON，如：[{\"key\":\"颜色\",\"value\":\"红色\"}]")
    private String spData;
}
