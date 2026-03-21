package com.gg.guimall.web.model.vo.oms;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * 提交订单请求 VO（前台）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("提交订单请求 VO")
public class SubmitOmsOrderReqVO {

    @NotNull(message = "会员ID不能为空")
    private Long memberId;

    @NotBlank(message = "收货人姓名不能为空")
    private String receiverName;

    @NotBlank(message = "收货人电话不能为空")
    private String receiverPhone;

    private String receiverPostCode;

    @NotBlank(message = "省不能为空")
    private String receiverProvince;

    @NotBlank(message = "市不能为空")
    private String receiverCity;

    @NotBlank(message = "区不能为空")
    private String receiverRegion;

    @NotBlank(message = "详细地址不能为空")
    private String receiverDetailAddress;

    /** 订单备注（可选） */
    private String note;

    /** 下单时间（可选，默认当前） */
    private LocalDateTime orderTime;
}

