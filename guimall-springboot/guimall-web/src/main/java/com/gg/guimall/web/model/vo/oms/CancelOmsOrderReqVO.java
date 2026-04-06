package com.gg.guimall.web.model.vo.oms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 取消订单请求 VO（前台）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("取消订单请求 VO")
public class CancelOmsOrderReqVO {

    @NotNull(message = "订单ID不能为空")
    @ApiModelProperty(value = "订单ID", required = true)
    private Long orderId;

    @NotNull(message = "会员ID不能为空")
    @ApiModelProperty(value = "会员ID", required = true)
    private Long memberId;
}
