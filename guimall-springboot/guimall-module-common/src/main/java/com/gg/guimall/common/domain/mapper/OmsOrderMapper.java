package com.gg.guimall.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.common.domain.dos.OmsOrderDO;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 * @description: 订单 Mapper
 **/
public interface OmsOrderMapper extends BaseMapper<OmsOrderDO> {

    default Page<OmsOrderDO> selectPageList(
            long current,
            long size,
            Long orderId,
            String orderSn,
            Long memberId,
            String memberUsername,
            Long farmerId,
            Integer status,
            LocalDateTime beginTime,
            LocalDateTime endTime
    ) {

        Page<OmsOrderDO> page = new Page<>(current, size);

        LambdaQueryWrapper<OmsOrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .eq(Objects.nonNull(orderId), OmsOrderDO::getId, orderId)
                .like(Objects.nonNull(orderSn) && orderSn.trim().length() > 0, OmsOrderDO::getOrderSn, orderSn)
                .eq(Objects.nonNull(memberId), OmsOrderDO::getMemberId, memberId)
                .like(Objects.nonNull(memberUsername) && memberUsername.trim().length() > 0, OmsOrderDO::getMemberUsername, memberUsername)
                .eq(Objects.nonNull(farmerId), OmsOrderDO::getFarmerId, farmerId)
                .eq(Objects.nonNull(status), OmsOrderDO::getStatus, status)
                .ge(Objects.nonNull(beginTime), OmsOrderDO::getCreateTime, beginTime)
                .le(Objects.nonNull(endTime), OmsOrderDO::getCreateTime, endTime)
                .eq(OmsOrderDO::getDeleteStatus, 0)
                .orderByDesc(OmsOrderDO::getCreateTime);

        return selectPage(page, wrapper);
    }
}

