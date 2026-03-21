package com.gg.guimall.web.model.vo.pms;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * SKU库存项 VO（前台）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("SKU库存项 VO")
public class ProductSkuStockItemVO {

    private Long id;

    private Long productId;

    private String skuCode;

    private BigDecimal price;

    private BigDecimal promotionPrice;

    private Integer stock;

    private Integer lowStock;

    private Integer lockStock;

    private String pic;

    private Integer sale;

    /**
     * 销售属性(JSON)
     */
    private String spData;
}

