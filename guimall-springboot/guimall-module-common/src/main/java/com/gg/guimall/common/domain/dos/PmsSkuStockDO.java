package com.gg.guimall.common.domain.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/18
 * @description: SKU库存 DO
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("pms_sku_stock")
public class PmsSkuStockDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long productId;

    /** SKU编码 */
    private String skuCode;

    /** 销售价格 */
    private BigDecimal price;

    /** 单品促销价格 */
    private BigDecimal promotionPrice;

    /** 库存 */
    private Integer stock;

    /** 预警库存 */
    private Integer lowStock;

    /** 锁定库存 */
    private Integer lockStock;

    /** 展示图片 */
    private String pic;

    /** 销量 */
    private Integer sale;

    /** 销售属性(JSON) */
    private String spData;
}
