package com.gg.guimall.admin.model.vo.sms;

import io.swagger.annotations.ApiModel;
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
 * @description: 优惠券详情响应 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "优惠券详情响应 VO")
public class FindSmsCouponDetailRspVO {

    private Long id;

    private Integer type;

    private String name;

    private Integer platform;

    private Integer count;

    private BigDecimal amount;

    private Integer perLimit;

    private BigDecimal minPoint;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer useType;

    private String note;

    private Integer publishCount;

    private Integer useCount;

    private Integer receiveCount;

    private LocalDateTime enableTime;

    private String code;

    private Integer memberLevel;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}

