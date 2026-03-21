package com.gg.guimall.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gg.guimall.common.domain.dos.SmsCouponDO;
import com.gg.guimall.common.domain.dos.SmsCouponHistoryDO;
import com.gg.guimall.common.domain.mapper.SmsCouponHistoryMapper;
import com.gg.guimall.common.domain.mapper.SmsCouponMapper;
import com.gg.guimall.common.enums.ResponseCodeEnum;
import com.gg.guimall.common.exception.BizException;
import com.gg.guimall.common.utils.Response;
import com.gg.guimall.web.model.vo.coupon.*;
import com.gg.guimall.web.service.SmsCouponService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/21
 * @description: 优惠券 Service 实现类
 **/
@Service
@Slf4j
public class SmsCouponServiceImpl implements SmsCouponService {

    @Autowired
    private SmsCouponMapper couponMapper;

    @Autowired
    private SmsCouponHistoryMapper couponHistoryMapper;

    @Override
    public Response findAvailableCouponList(Long memberId) {
        // 查询当前可领取的优惠券
        LocalDateTime now = LocalDateTime.now();
        LambdaQueryWrapper<SmsCouponDO> wrapper = Wrappers.lambdaQuery();
        wrapper.le(SmsCouponDO::getEnableTime, now)
                .ge(SmsCouponDO::getEndTime, now)
                .orderByDesc(SmsCouponDO::getCreateTime);

        List<SmsCouponDO> couponList = couponMapper.selectList(wrapper);

        if (CollectionUtils.isEmpty(couponList)) {
            return Response.success(new ArrayList<>());
        }

        // 查询会员已领取的优惠券
        LambdaQueryWrapper<SmsCouponHistoryDO> historyWrapper = Wrappers.lambdaQuery();
        historyWrapper.eq(SmsCouponHistoryDO::getMemberId, memberId);
        List<SmsCouponHistoryDO> historyList = couponHistoryMapper.selectList(historyWrapper);

        // 转换为响应VO
        List<FindCouponListRspVO> result = couponList.stream().map(coupon -> {
            FindCouponListRspVO vo = FindCouponListRspVO.builder()
                    .id(coupon.getId())
                    .type(coupon.getType())
                    .name(coupon.getName())
                    .platform(coupon.getPlatform())
                    .amount(coupon.getAmount())
                    .perLimit(coupon.getPerLimit())
                    .minPoint(coupon.getMinPoint())
                    .startTime(coupon.getStartTime())
                    .endTime(coupon.getEndTime())
                    .useType(coupon.getUseType())
                    .note(coupon.getNote())
                    .publishCount(coupon.getPublishCount())
                    .useCount(coupon.getUseCount())
                    .receiveCount(coupon.getReceiveCount())
                    .enableTime(coupon.getEnableTime())
                    .memberLevel(coupon.getMemberLevel())
                    .build();

            // 计算剩余数量
            int remainCount = coupon.getPublishCount() - coupon.getReceiveCount();
            vo.setRemainCount(Math.max(remainCount, 0));

            // 判断是否已领取
            long receivedCount = historyList.stream()
                    .filter(h -> h.getCouponId().equals(coupon.getId()))
                    .count();
            vo.setReceived(receivedCount > 0);

            return vo;
        }).collect(Collectors.toList());

        return Response.success(result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response receiveCoupon(ReceiveCouponReqVO reqVO) {
        Long couponId = reqVO.getCouponId();
        Long memberId = reqVO.getMemberId();

        // 查询优惠券信息
        SmsCouponDO coupon = couponMapper.selectById(couponId);
        if (coupon == null) {
            throw new BizException(ResponseCodeEnum.COUPON_NOT_EXIST);
        }

        // 检查优惠券是否在有效期内
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(coupon.getEnableTime())) {
            throw new BizException(ResponseCodeEnum.COUPON_NOT_START);
        }
        if (now.isAfter(coupon.getEndTime())) {
            throw new BizException(ResponseCodeEnum.COUPON_EXPIRED);
        }

        // 检查库存
        if (coupon.getReceiveCount() >= coupon.getPublishCount()) {
            throw new BizException(ResponseCodeEnum.COUPON_OUT_OF_STOCK);
        }

        // 检查会员领取次数限制
        LambdaQueryWrapper<SmsCouponHistoryDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SmsCouponHistoryDO::getCouponId, couponId)
                .eq(SmsCouponHistoryDO::getMemberId, memberId);
        Long receivedCount = couponHistoryMapper.selectCount(wrapper);

        if (receivedCount >= coupon.getPerLimit()) {
            throw new BizException(ResponseCodeEnum.COUPON_RECEIVE_LIMIT);
        }

        // 生成优惠券码
        String couponCode = generateCouponCode();

        // 创建领取记录
        SmsCouponHistoryDO history = SmsCouponHistoryDO.builder()
                .couponId(couponId)
                .memberId(memberId)
                .couponCode(couponCode)
                .memberNickname(reqVO.getMemberNickname())
                .getType(1) // 1用户领取
                .createTime(now)
                .useStatus(0) // 0未使用
                .build();

        couponHistoryMapper.insert(history);

        // 更新优惠券领取数量
        coupon.setReceiveCount(coupon.getReceiveCount() + 1);
        couponMapper.updateById(coupon);

        log.info("会员 {} 成功领取优惠券 {}", memberId, couponId);
        return Response.success("领取成功");
    }

    @Override
    public Response findMemberCouponList(Long memberId, Integer useStatus) {
        // 查询会员的优惠券领取记录
        LambdaQueryWrapper<SmsCouponHistoryDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SmsCouponHistoryDO::getMemberId, memberId);
        if (useStatus != null) {
            wrapper.eq(SmsCouponHistoryDO::getUseStatus, useStatus);
        }
        wrapper.orderByDesc(SmsCouponHistoryDO::getCreateTime);

        List<SmsCouponHistoryDO> historyList = couponHistoryMapper.selectList(wrapper);

        if (CollectionUtils.isEmpty(historyList)) {
            return Response.success(new ArrayList<>());
        }

        // 查询优惠券详情
        List<Long> couponIds = historyList.stream()
                .map(SmsCouponHistoryDO::getCouponId)
                .distinct()
                .collect(Collectors.toList());

        List<SmsCouponDO> couponList = couponMapper.selectBatchIds(couponIds);

        // 转换为响应VO
        List<FindMemberCouponListRspVO> result = historyList.stream().map(history -> {
            SmsCouponDO coupon = couponList.stream()
                    .filter(c -> c.getId().equals(history.getCouponId()))
                    .findFirst()
                    .orElse(null);

            if (coupon == null) {
                return null;
            }

            return FindMemberCouponListRspVO.builder()
                    .historyId(history.getId())
                    .couponId(coupon.getId())
                    .couponName(coupon.getName())
                    .couponCode(history.getCouponCode())
                    .amount(coupon.getAmount())
                    .minPoint(coupon.getMinPoint())
                    .startTime(coupon.getStartTime())
                    .endTime(coupon.getEndTime())
                    .useType(coupon.getUseType())
                    .useStatus(history.getUseStatus())
                    .receiveTime(history.getCreateTime())
                    .useTime(history.getUseTime())
                    .orderId(history.getOrderId())
                    .orderSn(history.getOrderSn())
                    .build();
        }).filter(vo -> vo != null).collect(Collectors.toList());

        return Response.success(result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response useCoupon(UseCouponReqVO reqVO) {
        // 查询优惠券领取记录
        SmsCouponHistoryDO history = couponHistoryMapper.selectById(reqVO.getHistoryId());
        if (history == null) {
            throw new BizException(ResponseCodeEnum.COUPON_HISTORY_NOT_EXIST);
        }

        // 验证会员ID
        if (!history.getMemberId().equals(reqVO.getMemberId())) {
            throw new BizException(ResponseCodeEnum.COUPON_NOT_BELONG_TO_MEMBER);
        }

        // 检查使用状态
        if (history.getUseStatus() != 0) {
            throw new BizException(ResponseCodeEnum.COUPON_ALREADY_USED);
        }

        // 查询优惠券信息
        SmsCouponDO coupon = couponMapper.selectById(history.getCouponId());
        if (coupon == null) {
            throw new BizException(ResponseCodeEnum.COUPON_NOT_EXIST);
        }

        // 检查优惠券是否过期
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(coupon.getEndTime())) {
            // 更新为已过期
            history.setUseStatus(2);
            couponHistoryMapper.updateById(history);
            throw new BizException(ResponseCodeEnum.COUPON_EXPIRED);
        }

        // 更新使用状态
        history.setUseStatus(1); // 1已使用
        history.setUseTime(now);
        history.setOrderId(reqVO.getOrderId());
        history.setOrderSn(reqVO.getOrderSn());
        couponHistoryMapper.updateById(history);

        // 更新优惠券使用数量
        coupon.setUseCount(coupon.getUseCount() + 1);
        couponMapper.updateById(coupon);

        log.info("会员 {} 成功使用优惠券 {}", reqVO.getMemberId(), history.getCouponId());
        return Response.success("使用成功");
    }

    @Override
    public Response findOrderAvailableCouponList(Long memberId, BigDecimal totalAmount) {
        // 查询会员未使用的优惠券
        LambdaQueryWrapper<SmsCouponHistoryDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(SmsCouponHistoryDO::getMemberId, memberId)
                .eq(SmsCouponHistoryDO::getUseStatus, 0) // 未使用
                .orderByDesc(SmsCouponHistoryDO::getCreateTime);

        List<SmsCouponHistoryDO> historyList = couponHistoryMapper.selectList(wrapper);

        if (CollectionUtils.isEmpty(historyList)) {
            return Response.success(new ArrayList<>());
        }

        // 查询优惠券详情
        List<Long> couponIds = historyList.stream()
                .map(SmsCouponHistoryDO::getCouponId)
                .distinct()
                .collect(Collectors.toList());

        List<SmsCouponDO> couponList = couponMapper.selectBatchIds(couponIds);

        LocalDateTime now = LocalDateTime.now();

        // 过滤可用的优惠券
        List<FindMemberCouponListRspVO> result = historyList.stream()
                .map(history -> {
                    SmsCouponDO coupon = couponList.stream()
                            .filter(c -> c.getId().equals(history.getCouponId()))
                            .findFirst()
                            .orElse(null);

                    if (coupon == null) {
                        return null;
                    }

                    // 检查是否过期
                    if (now.isAfter(coupon.getEndTime())) {
                        // 更新为已过期
                        history.setUseStatus(2);
                        couponHistoryMapper.updateById(history);
                        return null;
                    }

                    // 检查是否满足最低消费金额
                    if (totalAmount.compareTo(coupon.getMinPoint()) < 0) {
                        return null;
                    }

                    return FindMemberCouponListRspVO.builder()
                            .historyId(history.getId())
                            .couponId(coupon.getId())
                            .couponName(coupon.getName())
                            .couponCode(history.getCouponCode())
                            .amount(coupon.getAmount())
                            .minPoint(coupon.getMinPoint())
                            .startTime(coupon.getStartTime())
                            .endTime(coupon.getEndTime())
                            .useType(coupon.getUseType())
                            .useStatus(history.getUseStatus())
                            .receiveTime(history.getCreateTime())
                            .build();
                })
                .filter(vo -> vo != null)
                .collect(Collectors.toList());

        return Response.success(result);
    }

    /**
     * 生成优惠券码
     */
    private String generateCouponCode() {
        return "CPN" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
