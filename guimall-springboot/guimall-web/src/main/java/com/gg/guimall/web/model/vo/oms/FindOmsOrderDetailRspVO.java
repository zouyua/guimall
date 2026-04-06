package com.gg.guimall.web.model.vo.oms;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单详情响应 VO（前台）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("订单详情响应 VO")
public class FindOmsOrderDetailRspVO {

    private Long id;

    private String orderSn;

    private LocalDateTime createTime;

    private Long memberId;

    private Long farmerId;

    private BigDecimal totalAmount;

    private BigDecimal payAmount;

    private BigDecimal freightAmount;

    private BigDecimal couponAmount;

    private Long couponId;

    private String couponName;

    private Integer status;

    private String deliveryCompany;

    private String deliverySn;

    private String receiverName;

    private String receiverPhone;

    private String receiverDetailAddress;

    private String note;

    private List<OrderItem> items;

    @Data
    @Builder
    public static class OrderItem {
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

