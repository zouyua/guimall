package com.gg.guimall.web.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 订单超时延迟消息 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderTimeoutMessageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 订单ID */
    private Long orderId;

    /** 订单编号 */
    private String orderSn;
}
