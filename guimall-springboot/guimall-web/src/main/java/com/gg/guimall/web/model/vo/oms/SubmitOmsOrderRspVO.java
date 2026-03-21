package com.gg.guimall.web.model.vo.oms;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 提交订单响应 VO（前台）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("提交订单响应 VO")
public class SubmitOmsOrderRspVO {

    private Long orderId;

    private String orderSn;

    private BigDecimal totalAmount;

    private BigDecimal payAmount;

    private LocalDateTime createTime;
}

