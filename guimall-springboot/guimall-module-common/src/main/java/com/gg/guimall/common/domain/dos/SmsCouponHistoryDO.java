package com.gg.guimall.common.domain.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 * @description: 优惠券领取记录 DO（对应 sms_coupon_history）
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sms_coupon_history")
public class SmsCouponHistoryDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 优惠券ID
     */
    private Long couponId;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 优惠券码
     */
    private String couponCode;

    /**
     * 会员昵称
     */
    private String memberNickname;

    /**
     * 获取方式：0后台赠送1用户领取
     */
    private Integer getType;

    private LocalDateTime createTime;

    /**
     * 使用状态：0未使用1已使用2已过期
     */
    private Integer useStatus;

    /**
     * 使用时间
     */
    private LocalDateTime useTime;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单号
     */
    private String orderSn;
}

