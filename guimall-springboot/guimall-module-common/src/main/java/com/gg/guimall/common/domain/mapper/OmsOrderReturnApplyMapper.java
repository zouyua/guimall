package com.gg.guimall.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.common.domain.dos.OmsOrderReturnApplyDO;

import java.util.Objects;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 * @description: 退货申请 Mapper
 **/
public interface OmsOrderReturnApplyMapper extends BaseMapper<OmsOrderReturnApplyDO> {

    default Page<OmsOrderReturnApplyDO> selectPageList(
            long current,
            long size,
            Long orderId,
            String orderSn,
            String memberUsername,
            Integer status
    ) {
        Page<OmsOrderReturnApplyDO> page = new Page<>(current, size);

        LambdaQueryWrapper<OmsOrderReturnApplyDO> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .eq(Objects.nonNull(orderId), OmsOrderReturnApplyDO::getOrderId, orderId)
                .like(Objects.nonNull(orderSn) && orderSn.trim().length() > 0, OmsOrderReturnApplyDO::getOrderSn, orderSn)
                .like(Objects.nonNull(memberUsername) && memberUsername.trim().length() > 0, OmsOrderReturnApplyDO::getMemberUsername, memberUsername)
                .eq(Objects.nonNull(status), OmsOrderReturnApplyDO::getStatus, status)
                .orderByDesc(OmsOrderReturnApplyDO::getCreateTime);

        return selectPage(page, wrapper);
    }
}

