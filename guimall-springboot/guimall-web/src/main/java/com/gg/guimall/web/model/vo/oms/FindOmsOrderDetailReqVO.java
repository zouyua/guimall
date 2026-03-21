package com.gg.guimall.web.model.vo.oms;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 订单详情请求 VO（前台）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("订单详情请求 VO")
public class FindOmsOrderDetailReqVO {

    @NotNull(message = "订单ID不能为空")
    private Long id;

    @NotNull(message = "会员ID不能为空")
    private Long memberId;
}

