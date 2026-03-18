package com.gg.guimall.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gg.guimall.common.domain.dos.PmsSkuStockDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/18
 * @description: SKU库存 Mapper
 **/
@Mapper
public interface PmsSkuStockMapper extends BaseMapper<PmsSkuStockDO> {

    /** 根据商品ID查询SKU列表 */
    default List<PmsSkuStockDO> selectByProductId(Long productId) {
        return selectList(new LambdaQueryWrapper<PmsSkuStockDO>()
                .eq(PmsSkuStockDO::getProductId, productId)
                .orderByAsc(PmsSkuStockDO::getId));
    }

    /** 根据商品ID删除SKU */
    default int deleteByProductId(Long productId) {
        return delete(new LambdaQueryWrapper<PmsSkuStockDO>()
                .eq(PmsSkuStockDO::getProductId, productId));
    }
}
