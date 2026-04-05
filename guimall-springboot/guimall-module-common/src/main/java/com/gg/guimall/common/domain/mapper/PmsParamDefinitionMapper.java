package com.gg.guimall.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.common.domain.dos.PmsParamDefinitionDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品参数定义 Mapper
 */
public interface PmsParamDefinitionMapper extends BaseMapper<PmsParamDefinitionDO> {

    default List<PmsParamDefinitionDO> selectAllOrderBySort() {
        return selectList(new LambdaQueryWrapper<PmsParamDefinitionDO>()
                .orderByAsc(PmsParamDefinitionDO::getSort));
    }

    default Page<PmsParamDefinitionDO> selectPageList(Integer current, Integer size, String paramName) {
        Page<PmsParamDefinitionDO> page = new Page<>(current, size);
        LambdaQueryWrapper<PmsParamDefinitionDO> wrapper = new LambdaQueryWrapper<>();
        if (paramName != null && !paramName.trim().isEmpty()) {
            wrapper.like(PmsParamDefinitionDO::getParamName, paramName.trim());
        }
        wrapper.orderByAsc(PmsParamDefinitionDO::getSort);
        return selectPage(page, wrapper);
    }
}
