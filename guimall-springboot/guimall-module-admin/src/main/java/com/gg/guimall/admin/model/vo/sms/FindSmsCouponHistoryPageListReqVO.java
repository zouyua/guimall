package com.gg.guimall.admin.model.vo.sms;

import com.gg.guimall.common.model.BasePageQuery;
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
 * @description: 优惠券分页查询请求 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("优惠券领取记录分页查询请求 VO")
public class FindSmsCouponHistoryPageListReqVO extends BasePageQuery {

    @ApiModelProperty(value = "优惠券ID")
    private Long couponId;

    @ApiModelProperty(value = "会员ID")
    private Long memberId;

    @ApiModelProperty(value = "优惠券码（模糊）")
    private String couponCode;

    @ApiModelProperty(value = "会员昵称（模糊）")
    private String memberNickname;

    @ApiModelProperty(value = "使用状态：0未使用1已使用2已过期")
    private Integer useStatus;

    @ApiModelProperty(value = "使用开始时间（useTime）")
    private LocalDateTime beginUseTime;

    @ApiModelProperty(value = "使用结束时间（useTime）")
    private LocalDateTime endUseTime;
}

