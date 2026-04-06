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
 * @date: 2026/3/19
 * @description: 订单商品 DO（对应 oms_order_item）
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("oms_order_item")
public class OmsOrderItemDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long orderId;

    private String orderSn;

    private Long productId;

    private String productPic;

    private String productName;

    private BigDecimal productPrice;

    private Integer productQuantity;

    private Long productSkuId;

    private String productSkuCode;

    private String productAttr;

    private BigDecimal realAmount;
}

