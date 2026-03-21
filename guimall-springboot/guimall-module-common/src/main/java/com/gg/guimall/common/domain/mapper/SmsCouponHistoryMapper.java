package com.gg.guimall.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.common.domain.dos.SmsCouponHistoryDO;

import java.util.Objects;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 * @description: 优惠券领取记录 Mapper
 **/
public interface SmsCouponHistoryMapper extends BaseMapper<SmsCouponHistoryDO> {

    default Page<SmsCouponHistoryDO> selectPageList(
            long current,
            long size,
            Long couponId,
            Long memberId,
            String couponCode,
            String memberNickname,
            Integer useStatus,
            java.time.LocalDateTime beginUseTime,
            java.time.LocalDateTime endUseTime
    ) {
        Page<SmsCouponHistoryDO> page = new Page<>(current, size);

        LambdaQueryWrapper<SmsCouponHistoryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .eq(Objects.nonNull(couponId), SmsCouponHistoryDO::getCouponId, couponId)
                .eq(Objects.nonNull(memberId), SmsCouponHistoryDO::getMemberId, memberId)
                .like(Objects.nonNull(couponCode) && couponCode.trim().length() > 0, SmsCouponHistoryDO::getCouponCode, couponCode)
                .like(Objects.nonNull(memberNickname) && memberNickname.trim().length() > 0, SmsCouponHistoryDO::getMemberNickname, memberNickname)
                .eq(Objects.nonNull(useStatus), SmsCouponHistoryDO::getUseStatus, useStatus)
                .ge(Objects.nonNull(beginUseTime), SmsCouponHistoryDO::getUseTime, beginUseTime)
                .le(Objects.nonNull(endUseTime), SmsCouponHistoryDO::getUseTime, endUseTime)
                .orderByDesc(SmsCouponHistoryDO::getCreateTime);

        return selectPage(page, wrapper);
    }
}

