package com.gg.guimall.web.model.vo.oms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单列表分页响应 VO（前台）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("订单列表分页响应 VO")
public class FindOmsOrderPageListRspVO {

    @ApiModelProperty(value = "订单ID")
    private Long id;

    @ApiModelProperty(value = "订单编号")
    private String orderSn;

    @ApiModelProperty(value = "下单时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "会员ID")
    private Long memberId;

    @ApiModelProperty(value = "订单总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "实际支付金额")
    private BigDecimal payAmount;

    @ApiModelProperty(value = "订单状态：0待付款；1待发货；2已发货；3已完成；4已关闭；5无效订单")
    private Integer status;

    @ApiModelProperty(value = "订单商品列表")
    private List<OrderItemVO> items;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OrderItemVO {
        private Long id;
        private Long productId;
        private String productName;
        private String productPic;
        private Long productSkuId;
        private String productSkuCode;
        private BigDecimal productPrice;
        private Integer productQuantity;
        private BigDecimal realAmount;
        private String productAttr;
    }
}

