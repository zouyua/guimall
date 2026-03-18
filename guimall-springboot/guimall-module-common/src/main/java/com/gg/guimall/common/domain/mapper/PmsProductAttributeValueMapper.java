package com.gg.guimall.common.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gg.guimall.common.domain.dos.PmsProductAttributeValueDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/13
 * @description: 商品属性值 Mapper
 **/
@Mapper
public interface PmsProductAttributeValueMapper extends BaseMapper<PmsProductAttributeValueDO> {

    /**
     * 根据商品ID查询属性值列表
     */
    default List<PmsProductAttributeValueDO> selectByProductId(Long productId) {
        LambdaQueryWrapper<PmsProductAttributeValueDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PmsProductAttributeValueDO::getProductId, productId);
        return selectList(wrapper);
    }

    /**
     * 根据商品ID删除属性值
     */
    default int deleteByProductId(Long productId) {
        LambdaQueryWrapper<PmsProductAttributeValueDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PmsProductAttributeValueDO::getProductId, productId);
        return delete(wrapper);
    }
}
