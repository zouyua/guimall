package com.gg.guimall.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gg.guimall.common.domain.dos.PmsSkuSpecDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * SKU 规格明细 Mapper
 *
 * @author wly
 */
@Mapper
public interface PmsSkuSpecMapper extends BaseMapper<PmsSkuSpecDO> {

    /** 根据 SKU ID 查询规格列表（按 sort 升序） */
    default List<PmsSkuSpecDO> selectBySkuId(Long skuId) {
        return selectList(new LambdaQueryWrapper<PmsSkuSpecDO>()
                .eq(PmsSkuSpecDO::getSkuId, skuId)
                .orderByAsc(PmsSkuSpecDO::getSort));
    }

    /** 根据商品 ID 查询所有 SKU 的规格（批量加载用） */
    default List<PmsSkuSpecDO> selectByProductId(Long productId) {
        return selectList(new LambdaQueryWrapper<PmsSkuSpecDO>()
                .eq(PmsSkuSpecDO::getProductId, productId)
                .orderByAsc(PmsSkuSpecDO::getSkuId)
                .orderByAsc(PmsSkuSpecDO::getSort));
    }

    /** 根据 SKU ID 删除规格 */
    default int deleteBySkuId(Long skuId) {
        return delete(new LambdaQueryWrapper<PmsSkuSpecDO>()
                .eq(PmsSkuSpecDO::getSkuId, skuId));
    }

    /** 根据商品 ID 删除该商品下所有 SKU 的规格 */
    default int deleteByProductId(Long productId) {
        return delete(new LambdaQueryWrapper<PmsSkuSpecDO>()
                .eq(PmsSkuSpecDO::getProductId, productId));
    }
}
