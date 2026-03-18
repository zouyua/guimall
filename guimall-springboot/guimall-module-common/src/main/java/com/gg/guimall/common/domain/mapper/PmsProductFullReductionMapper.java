package com.gg.guimall.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gg.guimall.common.domain.dos.PmsProductFullReductionDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/18
 * @description: 商品满减 Mapper
 **/
@Mapper
public interface PmsProductFullReductionMapper extends BaseMapper<PmsProductFullReductionDO> {

    /** 根据商品ID查询满减列表 */
    default List<PmsProductFullReductionDO> selectByProductId(Long productId) {
        return selectList(new LambdaQueryWrapper<PmsProductFullReductionDO>()
                .eq(PmsProductFullReductionDO::getProductId, productId)
                .orderByAsc(PmsProductFullReductionDO::getFullPrice));
    }

    /** 根据商品ID删除满减规则 */
    default int deleteByProductId(Long productId) {
        return delete(new LambdaQueryWrapper<PmsProductFullReductionDO>()
                .eq(PmsProductFullReductionDO::getProductId, productId));
    }
}
