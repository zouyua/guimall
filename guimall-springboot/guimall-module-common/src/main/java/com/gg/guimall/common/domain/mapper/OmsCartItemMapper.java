package com.gg.guimall.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gg.guimall.common.domain.dos.OmsCartItemDO;

import java.util.List;
import java.util.Objects;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 * @description: 购物车 Mapper
 **/
public interface OmsCartItemMapper extends BaseMapper<OmsCartItemDO> {

    default List<OmsCartItemDO> selectByMemberId(Long memberId) {
        return selectList(new LambdaQueryWrapper<OmsCartItemDO>()
                .eq(OmsCartItemDO::getMemberId, memberId)
                .eq(OmsCartItemDO::getIsDeleted, 0)
                .orderByDesc(OmsCartItemDO::getUpdateTime)
                .orderByDesc(OmsCartItemDO::getId));
    }

    default OmsCartItemDO selectOneByMemberSkuAndAttr(Long memberId, Long productSkuId, String productAttr) {
        LambdaQueryWrapper<OmsCartItemDO> wrapper = new LambdaQueryWrapper<OmsCartItemDO>()
                .eq(OmsCartItemDO::getMemberId, memberId)
                .eq(OmsCartItemDO::getProductSkuId, productSkuId)
                .eq(OmsCartItemDO::getIsDeleted, 0);
        if (Objects.nonNull(productAttr) && productAttr.trim().length() > 0) {
            wrapper.eq(OmsCartItemDO::getProductAttr, productAttr);
        } else {
            wrapper.and(w -> w.isNull(OmsCartItemDO::getProductAttr).or().eq(OmsCartItemDO::getProductAttr, ""));
        }
        return selectOne(wrapper);
    }

    default int clearByMemberId(Long memberId) {
        return delete(new LambdaQueryWrapper<OmsCartItemDO>()
                .eq(OmsCartItemDO::getMemberId, memberId)
                .eq(OmsCartItemDO::getIsDeleted, 0));
    }
}

