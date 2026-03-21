package com.gg.guimall.web.model.vo.coupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/21
 * @description: 会员优惠券列表响应 VO
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("会员优惠券列表响应 VO")
public class FindMemberCouponListRspVO {

    @ApiModelProperty(value = "优惠券领取记录ID")
    private Long historyId;

    @ApiModelProperty(value = "优惠券ID")
    private Long couponId;

    @ApiModelProperty(value = "优惠券名称")
    private String couponName;

    @ApiModelProperty(value = "优惠券码")
    private String couponCode;

    @ApiModelProperty(value = "优惠金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "最低消费金额")
    private BigDecimal minPoint;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "使用范围：0全场1指定分类2指定商品")
    private Integer useType;

    @ApiModelProperty(value = "使用状态：0未使用1已使用2已过期")
    private Integer useStatus;

    @ApiModelProperty(value = "领取时间")
    private LocalDateTime receiveTime;

    @ApiModelProperty(value = "使用时间")
    private LocalDateTime useTime;

    @ApiModelProperty(value = "订单ID")
    private Long orderId;

    @ApiModelProperty(value = "订单号")
    private String orderSn;
}
