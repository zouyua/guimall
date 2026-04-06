package com.gg.guimall.admin.model.vo.sms;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 * @description: 创建优惠券请求 VO（对应 sms_coupon）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "创建优惠券请求 VO")
public class CreateSmsCouponReqVO {

    /** 优惠券类型：0全场赠券1会员赠券2购物赠券3注册赠券 */
    @NotNull(message = "优惠券类型不能为空")
    private Integer type;

    /** 优惠券名称 */
    @NotNull(message = "优惠券名称不能为空")
    @Size(max = 100, message = "优惠券名称最长100个字符")
    private String name;

    /** 使用平台：0全部1移动端2WEB */
    @NotNull(message = "使用平台不能为空")
    private Integer platform;

    /** 总数量 */
    @NotNull(message = "总数量不能为空")
    @Positive(message = "总数量必须为正数")
    private Integer count;

    /** 优惠金额 */
    @NotNull(message = "优惠金额不能为空")
    private BigDecimal amount;

    /** 每人限领数量 */
    @NotNull(message = "每人限领数量不能为空")
    @Positive(message = "每人限领数量必须为正数")
    private Integer perLimit;

    /** 使用门槛（满X元可用，0表示无门槛）*/
    @NotNull(message = "最低消费金额不能为空")
    private BigDecimal minAmount;

    /** 开始时间 */
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;

    /** 结束时间 */
    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;

    /** 使用范围：0全场1指定分类2指定商品 */
    @NotNull(message = "使用范围不能为空")
    private Integer useType;

    /** 备注 */
    @Size(max = 200, message = "备注最长200个字符")
    private String note;

    /** 领取开始时间 */
    private LocalDateTime enableTime;

    /** 优惠码 */
    @Size(max = 64, message = "优惠码最长64个字符")
    private String code;

    /** 会员等级限制 */
    private Integer memberLevel;
}

