package com.gg.guimall.common.domain.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @description: 优惠券 DO（对应 sms_coupon）
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sms_coupon")
public class SmsCouponDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 优惠券类型：0全场赠券1会员赠券2购物赠券3注册赠券
     */
    private Integer type;

    /**
     * 优惠券名称
     */
    private String name;

    /**
     * 使用平台：0全部1移动端2WEB
     */
    private Integer platform;

    /**
     * 优惠金额
     */
    private BigDecimal amount;

    /**
     * 每人限领数量
     */
    private Integer perLimit;

    /**
     * 使用门槛（满X元可用，0表示无门槛）
     */
    private BigDecimal minAmount;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 使用范围：0全场1指定分类2指定商品
     */
    private Integer useType;

    /**
     * 备注
     */
    private String note;

    /**
     * 发行数量
     */
    private Integer publishCount;

    /**
     * 发放总数量（0不限）
     */
    private Integer totalCount;

    /**
     * 已使用数量
     */
    private Integer useCount;

    /**
     * 领取数量
     */
    private Integer receiveCount;

    /**
     * 领取开始时间
     */
    private LocalDateTime enableTime;

    /**
     * 优惠码
     */
    private String code;

    /**
     * 会员等级限制
     */
    private Integer memberLevel;

    /**
     * 状态：0禁用 1启用
     */
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}

