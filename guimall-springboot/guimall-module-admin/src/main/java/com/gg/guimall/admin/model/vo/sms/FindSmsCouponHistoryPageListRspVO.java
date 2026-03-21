package com.gg.guimall.admin.model.vo.sms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 * @description:优惠券领取记录分页列表响应 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("优惠券领取记录分页列表响应 VO")
public class FindSmsCouponHistoryPageListRspVO {

    @ApiModelProperty(value = "优惠券领取记录ID")
    private Long id;

    @ApiModelProperty(value = "优惠券ID")
    private Long couponId;

    @ApiModelProperty(value = "会员ID")
    private Long memberId;

    @ApiModelProperty(value = "优惠券码")
    private String couponCode;

    @ApiModelProperty(value = "会员昵称")
    private String memberNickname;

    @ApiModelProperty(value = "获取方式：0后台赠送1用户领取")
    private Integer getType;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "使用状态：0未使用1已使用2已过期")
    private Integer useStatus;

    @ApiModelProperty(value = "使用时间")
    private LocalDateTime useTime;

    @ApiModelProperty(value = "订单ID")
    private Long orderId;

    @ApiModelProperty(value = "订单号")
    private String orderSn;
}

