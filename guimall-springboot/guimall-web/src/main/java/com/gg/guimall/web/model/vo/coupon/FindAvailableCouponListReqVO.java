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
 * @description: 查询可用优惠券列表请求 VO
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("查询可用优惠券列表请求 VO")
public class FindAvailableCouponListReqVO {

    @ApiModelProperty(value = "会员ID", required = true)
    @NotNull(message = "会员ID不能为空")
    private Long memberId;

    @ApiModelProperty(value = "使用类型：0全场1指定分类2指定商品")
    private Integer useType;
}
