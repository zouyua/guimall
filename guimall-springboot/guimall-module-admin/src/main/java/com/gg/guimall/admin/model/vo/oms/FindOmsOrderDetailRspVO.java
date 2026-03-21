package com.gg.guimall.admin.model.vo.oms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 * @description: 订单详情响应 VO
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("订单详情 VO")
public class FindOmsOrderDetailRspVO {

    @ApiModelProperty(value = "订单ID")
    private Long id;

    @ApiModelProperty(value = "订单编号")
    private String orderSn;

    @ApiModelProperty(value = "下单时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "会员ID")
    private Long memberId;

    @ApiModelProperty(value = "会员账号")
    private String memberUsername;

    @ApiModelProperty(value = "农户ID")
    private Long farmerId;

    @ApiModelProperty(value = "订单总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "实际支付金额")
    private BigDecimal payAmount;

    @ApiModelProperty(value = "运费")
    private BigDecimal freightAmount;

    @ApiModelProperty(value = "订单状态：0待付款；1待发货；2已发货；3已完成；4已关闭；5无效订单")
    private Integer status;

    @ApiModelProperty(value = "物流公司")
    private String deliveryCompany;

    @ApiModelProperty(value = "物流单号")
    private String deliverySn;

    @ApiModelProperty(value = "收货人姓名")
    private String receiverName;

    @ApiModelProperty(value = "收货人电话")
    private String receiverPhone;

    @ApiModelProperty(value = "收货地址")
    private String receiverDetailAddress;

    @ApiModelProperty(value = "订单备注")
    private String note;

    @ApiModelProperty(value = "订单商品明细")
    private List<OrderItem> items;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ApiModel("订单商品明细")
    public static class OrderItem {
        @ApiModelProperty(value = "订单商品ID")
        private Long id;

        @ApiModelProperty(value = "商品ID")
        private Long productId;

        @ApiModelProperty(value = "商品名称")
        private String productName;

        @ApiModelProperty(value = "商品图片")
        private String productPic;

        @ApiModelProperty(value = "SKU ID")
        private Long productSkuId;

        @ApiModelProperty(value = "SKU 编码")
        private String productSkuCode;

        @ApiModelProperty(value = "商品单价")
        private BigDecimal productPrice;

        @ApiModelProperty(value = "购买数量")
        private Integer productQuantity;

        @ApiModelProperty(value = "最终金额")
        private BigDecimal realAmount;

        @ApiModelProperty(value = "商品销售属性(JSON)")
        private String productAttr;
    }
}

