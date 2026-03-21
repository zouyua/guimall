package com.gg.guimall.admin.model.vo.oms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 * @description: 订单列表分页查询响应 VO
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("订单列表分页查询响应 VO")
public class FindOmsOrderPageListRspVO {

    @ApiModelProperty(value = "订单ID")
    private Long id;

    @ApiModelProperty(value = "订单编号")
    private String orderSn;

    @ApiModelProperty(value = "下单时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "会员ID")
    private Long memberId;

    @ApiModelProperty(value = "会员账号")
    private String memberUsername;

    @ApiModelProperty(value = "农户ID")
    private Long farmerId;

    @ApiModelProperty(value = "订单总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "实际支付金额")
    private BigDecimal payAmount;

    @ApiModelProperty(value = "订单状态：0待付款；1待发货；2已发货；3已完成；4已关闭；5无效订单")
    private Integer status;
}

