package com.gg.guimall.admin.model.vo.sms;

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
 * @date: 2026/3/19
 * @description:优惠券分页列表响应 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("优惠券分页列表响应 VO")
public class FindSmsCouponPageListRspVO {

    @ApiModelProperty(value = "优惠券ID")
    private Long id;

    @ApiModelProperty(value = "优惠券类型")
    private Integer type;

    @ApiModelProperty(value = "优惠券名称")
    private String name;

    @ApiModelProperty(value = "使用平台")
    private Integer platform;

    @ApiModelProperty(value = "总数量")
    private Integer count;

    @ApiModelProperty(value = "优惠金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "每人限领数量")
    private Integer perLimit;

    @ApiModelProperty(value = "使用门槛金额")
    private BigDecimal minAmount;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "使用范围")
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

    @ApiModelProperty(value = "优惠码")
    private String code;

    @ApiModelProperty(value = "会员等级限制")
    private Integer memberLevel;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}

