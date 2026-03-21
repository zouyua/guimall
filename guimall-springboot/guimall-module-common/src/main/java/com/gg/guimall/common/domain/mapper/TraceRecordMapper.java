package com.gg.guimall.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gg.guimall.common.domain.dos.TraceRecordDO;

import java.util.List;
import java.util.Objects;

/**
 * 溯源记录 Mapper trace_record
 *
 * @author wly
 * @url www.gg.com
 * @date 2026/3/19
 */
public interface TraceRecordMapper extends BaseMapper<TraceRecordDO> {

    default List<TraceRecordDO> selectByProductId(Long productId) {
        return selectList(new LambdaQueryWrapper<TraceRecordDO>()
                .eq(Objects.nonNull(productId), TraceRecordDO::getProductId, productId)
                .orderByDesc(TraceRecordDO::getRecordTime)
                .orderByDesc(TraceRecordDO::getCreateTime));
    }
}
