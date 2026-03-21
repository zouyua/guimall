package com.gg.guimall.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gg.guimall.common.domain.dos.TraceProductOriginDO;

import java.util.List;
import java.util.Objects;

/**
 * 商品产地关联表 Mapper trace_product_origin
 *
 * @author wly
 * @url www.gg.com
 * @date 2026/3/19
 */
public interface TraceProductOriginMapper extends BaseMapper<TraceProductOriginDO> {

    default TraceProductOriginDO selectByProductId(Long productId) {
        if (Objects.isNull(productId) || productId <= 0) {
            return null;
        }
        return selectOne(new LambdaQueryWrapper<TraceProductOriginDO>()
                .eq(TraceProductOriginDO::getProductId, productId)
                .orderByDesc(TraceProductOriginDO::getCreateTime)
                .last("limit 1"));
    }

    default List<TraceProductOriginDO> selectByOriginId(Long originId) {
        return selectList(new LambdaQueryWrapper<TraceProductOriginDO>()
                .eq(Objects.nonNull(originId), TraceProductOriginDO::getOriginId, originId)
                .orderByDesc(TraceProductOriginDO::getCreateTime));
    }
}
