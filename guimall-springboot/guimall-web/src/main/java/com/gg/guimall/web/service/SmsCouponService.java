package com.gg.guimall.web.service;

import com.gg.guimall.common.utils.Response;
import com.gg.guimall.web.model.vo.coupon.*;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/21
 * @description: 优惠券 Service 接口
 **/
public interface SmsCouponService {

    /**
     * 获取可领取的优惠券列表
     * @param memberId 会员ID
     * @return 优惠券列表
     */
    Response findAvailableCouponList(Long memberId);

    /**
     * 领取优惠券
     * @param reqVO 领取请求
     * @return 领取结果
     */
    Response receiveCoupon(ReceiveCouponReqVO reqVO);

    /**
     * 获取会员的优惠券列表
     * @param memberId 会员ID
     * @param useStatus 使用状态：0未使用1已使用2已过期，null查询全部
     * @return 会员优惠券列表
     */
    Response findMemberCouponList(Long memberId, Integer useStatus);

    /**
     * 使用优惠券
     * @param reqVO 使用请求
     * @return 使用结果
     */
    Response useCoupon(UseCouponReqVO reqVO);

    /**
     * 获取订单可用的优惠券列表
     * @param memberId 会员ID
     * @param totalAmount 订单总金额
     * @return 可用优惠券列表
     */
    Response findOrderAvailableCouponList(Long memberId, java.math.BigDecimal totalAmount);
}
