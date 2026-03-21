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
 * @description: 领取优惠券请求 VO
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("领取优惠券请求 VO")
public class ReceiveCouponReqVO {

    @ApiModelProperty(value = "优惠券ID", required = true)
    @NotNull(message = "优惠券ID不能为空")
    private Long couponId;

    @ApiModelProperty(value = "会员ID", required = true)
    @NotNull(message = "会员ID不能为空")
    private Long memberId;

    @ApiModelProperty(value = "会员昵称")
    private String memberNickname;
}
