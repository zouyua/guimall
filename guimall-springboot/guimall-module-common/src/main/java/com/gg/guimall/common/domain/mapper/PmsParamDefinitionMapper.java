package com.gg.guimall.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gg.guimall.common.domain.dos.PmsParamDefinitionDO;

import java.util.List;

/**
 * 商品参数定义 Mapper
 */
public interface PmsParamDefinitionMapper extends BaseMapper<PmsParamDefinitionDO> {

    default List<PmsParamDefinitionDO> selectByCategoryId(Long categoryId) {
        return selectList(new LambdaQueryWrapper<PmsParamDefinitionDO>()
                .eq(PmsParamDefinitionDO::getCategoryId, categoryId)
                .orderByAsc(PmsParamDefinitionDO::getSort));
    }
}
