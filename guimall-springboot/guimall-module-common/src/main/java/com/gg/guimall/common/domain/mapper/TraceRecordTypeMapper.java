package com.gg.guimall.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gg.guimall.common.domain.dos.TraceRecordTypeDO;

import java.util.List;
import java.util.Objects;

/**
 * 溯源记录类型 Mapper
 *
 * @author wly
 * @url www.gg.com
 * @date 2026/04/03
 */
public interface TraceRecordTypeMapper extends BaseMapper<TraceRecordTypeDO> {

    /**
     * 根据商品分类ID查询溯源记录类型列表
     */
    default List<TraceRecordTypeDO> selectByCategoryId(Long categoryId) {
        return selectList(new LambdaQueryWrapper<TraceRecordTypeDO>()
                .eq(Objects.nonNull(categoryId), TraceRecordTypeDO::getCategoryId, categoryId)
                .orderByAsc(TraceRecordTypeDO::getSort)
                .orderByAsc(TraceRecordTypeDO::getId));
    }

    /**
     * 查询所有溯源记录类型
     */
    default List<TraceRecordTypeDO> selectAllTypes() {
        return selectList(new LambdaQueryWrapper<TraceRecordTypeDO>()
                .orderByAsc(TraceRecordTypeDO::getCategoryId)
                .orderByAsc(TraceRecordTypeDO::getSort));
    }
}
