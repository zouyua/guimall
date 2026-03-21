package com.gg.guimall.admin.model.vo.oms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 * @description: 发货请求 VO
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("订单发货请求 VO")
public class DeliverOmsOrderReqVO {

    @ApiModelProperty(value = "订单ID")
    @NotNull(message = "订单ID不能为空")
    private Long id;

    @ApiModelProperty(value = "物流公司")
    @NotBlank(message = "物流公司不能为空")
    private String deliveryCompany;

    @ApiModelProperty(value = "物流单号")
    @NotBlank(message = "物流单号不能为空")
    private String deliverySn;
}

