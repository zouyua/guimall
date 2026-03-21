package com.gg.guimall.admin.model.vo.oms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 * @description: 订单备注请求 VO
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("订单备注请求 VO")
public class RemarkOmsOrderReqVO {

    @ApiModelProperty(value = "订单ID")
    @NotNull(message = "订单ID不能为空")
    private Long id;

    @ApiModelProperty(value = "订单备注")
    private String note;
}

