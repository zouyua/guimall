package com.gg.guimall.web.model.vo.coupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/21
 * @description: 使用优惠券请求 VO
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("使用优惠券请求 VO")
public class UseCouponReqVO {

    @ApiModelProperty(value = "优惠券领取记录ID", required = true)
    @NotNull(message = "优惠券领取记录ID不能为空")
    private Long historyId;

    @ApiModelProperty(value = "订单ID", required = true)
    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @ApiModelProperty(value = "订单号", required = true)
    @NotNull(message = "订单号不能为空")
    private String orderSn;

    @ApiModelProperty(value = "会员ID", required = true)
    @NotNull(message = "会员ID不能为空")
    private Long memberId;
}
