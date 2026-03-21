package com.gg.guimall.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gg.guimall.common.domain.dos.OmsOrderItemDO;

import java.util.List;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 * @description: 订单商品 Mapper
 **/
public interface OmsOrderItemMapper extends BaseMapper<OmsOrderItemDO> {

    /**
     * 根据订单ID查询订单商品明细
     */
    default List<OmsOrderItemDO> selectByOrderId(Long orderId) {
        return selectList(new LambdaQueryWrapper<OmsOrderItemDO>()
                .eq(OmsOrderItemDO::getOrderId, orderId)
                .orderByDesc(OmsOrderItemDO::getId));
    }
}

