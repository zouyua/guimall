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
 * @description: 优惠券列表响应 VO
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("优惠券列表响应 VO")
public class FindCouponListRspVO {

    @ApiModelProperty(value = "优惠券ID")
    private Long id;

    @ApiModelProperty(value = "优惠券类型：0全场赠券1会员赠券2购物赠券3注册赠券")
    private Integer type;

    @ApiModelProperty(value = "优惠券名称")
    private String name;

    @ApiModelProperty(value = "使用平台：0全部1移动端2WEB")
    private Integer platform;

    @ApiModelProperty(value = "优惠金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "每人限领数量")
    private Integer perLimit;

    @ApiModelProperty(value = "最低消费金额")
    private BigDecimal minPoint;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "使用范围：0全场1指定分类2指定商品")
    private Integer useType;

    @ApiModelProperty(value = "备注")
    private String note;

    @ApiModelProperty(value = "发行数量")
    private Integer publishCount;

    @ApiModelProperty(value = "已使用数量")
    private Integer useCount;

    @ApiModelProperty(value = "领取数量")
    private Integer receiveCount;

    @ApiModelProperty(value = "领取开始时间")
    private LocalDateTime enableTime;

    @ApiModelProperty(value = "会员等级限制")
    private Integer memberLevel;

    @ApiModelProperty(value = "是否已领取")
    private Boolean received;

    @ApiModelProperty(value = "剩余数量")
    private Integer remainCount;
}
