package com.gg.guimall.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gg.guimall.common.domain.dos.PmsSkuStockDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

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

    /** 锁定库存（扣减可用库存，增加锁定库存） */
    @Update("UPDATE pms_sku_stock SET stock = stock - #{quantity}, lock_stock = lock_stock + #{quantity} " +
            "WHERE id = #{skuId} AND stock >= #{quantity}")
    int lockStock(@Param("skuId") Long skuId, @Param("quantity") Integer quantity);

    /** 解锁库存（增加可用库存，减少锁定库存） */
    @Update("UPDATE pms_sku_stock SET stock = stock + #{quantity}, lock_stock = lock_stock - #{quantity} " +
            "WHERE id = #{skuId} AND lock_stock >= #{quantity}")
    int unlockStock(@Param("skuId") Long skuId, @Param("quantity") Integer quantity);

    /** 扣减锁定库存（支付成功后，只减少锁定库存） */
    @Update("UPDATE pms_sku_stock SET lock_stock = lock_stock - #{quantity}, sale = sale + #{quantity} " +
            "WHERE id = #{skuId} AND lock_stock >= #{quantity}")
    int deductLockStock(@Param("skuId") Long skuId, @Param("quantity") Integer quantity);

    /** 恢复库存（取消订单或退货，增加可用库存，减少销量） */
    @Update("UPDATE pms_sku_stock SET stock = stock + #{quantity}, sale = sale - #{quantity} " +
            "WHERE id = #{skuId} AND sale >= #{quantity}")
    int restoreStock(@Param("skuId") Long skuId, @Param("quantity") Integer quantity);
}
