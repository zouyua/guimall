package com.gg.guimall.web.model.vo.oms;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 提交订单请求 VO（前台）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("提交订单请求 VO")
public class SubmitOmsOrderReqVO {

    @NotNull(message = "会员ID不能为空")
    private Long memberId;

    @NotBlank(message = "收货人姓名不能为空")
    private String receiverName;

    @NotBlank(message = "收货人电话不能为空")
    private String receiverPhone;

    private String receiverPostCode;

    @NotBlank(message = "省不能为空")
    private String receiverProvince;

    @NotBlank(message = "市不能为空")
    private String receiverCity;

    @NotBlank(message = "区不能为空")
    private String receiverRegion;

    @NotBlank(message = "详细地址不能为空")
    private String receiverDetailAddress;

    /** 优惠券ID（可选） */
    private Long couponId;

    /** 订单备注（可选） */
    private String note;

    /** 下单时间（可选，默认当前） */
    private LocalDateTime orderTime;

    /** 订单商品列表 */
    @NotEmpty(message = "订单商品不能为空")
    private List<OrderItemVO> items;

    /** 订单总金额 */
    @NotNull(message = "订单总金额不能为空")
    private BigDecimal totalAmount;

    /** 运费金额 */
    private BigDecimal freightAmount;

    /** 应付金额 */
    @NotNull(message = "应付金额不能为空")
    private BigDecimal payAmount;

    /** 是否从购物车结算（true=从购物车，false=直接购买） */
    private Boolean fromCart;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderItemVO {
        private Long productId;
        private Long productSkuId;
        private String productName;
        private String productPic;
        private String productAttr;
        private BigDecimal price;
        private Integer quantity;
        private Long cartItemId; // 购物车项ID，用于删除
    }
}

