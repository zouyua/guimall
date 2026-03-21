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
 * @description: 退货申请分页查询响应 VO
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("退货申请分页查询响应 VO")
public class FindOmsOrderReturnApplyPageRspVO {

    @ApiModelProperty(value = "退货申请ID")
    private Long id;

    @ApiModelProperty(value = "订单ID")
    private Long orderId;

    @ApiModelProperty(value = "订单编号")
    private String orderSn;

    @ApiModelProperty(value = "申请时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "会员账号")
    private String memberUsername;

    @ApiModelProperty(value = "退货金额")
    private BigDecimal returnAmount;

    @ApiModelProperty(value = "状态：0->待处理；1->退货中；2->已完成；3->已拒绝")
    private Integer status;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "商品数量")
    private Integer productCount;
}

