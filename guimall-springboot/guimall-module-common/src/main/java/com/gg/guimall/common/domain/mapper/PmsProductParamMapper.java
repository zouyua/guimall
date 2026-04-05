package com.gg.guimall.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gg.guimall.common.domain.dos.PmsProductParamDO;

import java.util.List;

/**
 * 商品参数 Mapper
 */
public interface PmsProductParamMapper extends BaseMapper<PmsProductParamDO> {

    default List<PmsProductParamDO> selectByProductId(Long productId) {
        return selectList(new LambdaQueryWrapper<PmsProductParamDO>()
                .eq(PmsProductParamDO::getProductId, productId)
                .orderByAsc(PmsProductParamDO::getSort));
    }

    default int deleteByProductId(Long productId) {
        return delete(new LambdaQueryWrapper<PmsProductParamDO>()
                .eq(PmsProductParamDO::getProductId, productId));
    }
}
