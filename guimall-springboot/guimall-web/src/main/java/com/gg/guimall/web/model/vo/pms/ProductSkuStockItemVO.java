package com.gg.guimall.web.model.vo.pms;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

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
     * 规格列表（替代 spData JSON 字段）
     */
    private List<SkuSpecItemVO> specs;

    @Deprecated
    /**
     * 销售属性JSON（已废弃，请使用 specs 字段）
     */
    private String spData;
}

