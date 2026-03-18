package com.gg.guimall.common.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gg.guimall.common.domain.dos.PmsProductAttributeDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Objects;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/13
 * @description: 商品属性 Mapper
 **/
@Mapper
public interface PmsProductAttributeMapper extends BaseMapper<PmsProductAttributeDO> {

    /**
     * 根据属性分类ID查询属性列表
     */
    default List<PmsProductAttributeDO> selectByCategoryId(Long categoryId, Integer type) {
        LambdaQueryWrapper<PmsProductAttributeDO> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .eq(Objects.nonNull(categoryId), PmsProductAttributeDO::getProductAttributeCategoryId, categoryId)
                .eq(Objects.nonNull(type), PmsProductAttributeDO::getType, type)
                .orderByAsc(PmsProductAttributeDO::getSort);
        return selectList(wrapper);
    }
}
