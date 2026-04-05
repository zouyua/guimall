package com.gg.guimall.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gg.guimall.common.domain.dos.PmsFarmerOriginDO;

import java.util.List;

/**
 * 农户-产地关联 Mapper
 *
 * @author wly
 * @date 2026/4/4
 */
public interface PmsFarmerOriginMapper extends BaseMapper<PmsFarmerOriginDO> {

    /**
     * 根据农户ID查询关联的产地列表
     */
    default List<PmsFarmerOriginDO> selectByFarmerId(Long farmerId) {
        return selectList(new LambdaQueryWrapper<PmsFarmerOriginDO>()
                .eq(PmsFarmerOriginDO::getFarmerId, farmerId));
    }

    /**
     * 根据产地ID查询关联的农户列表
     */
    default List<PmsFarmerOriginDO> selectByOriginId(Long originId) {
        return selectList(new LambdaQueryWrapper<PmsFarmerOriginDO>()
                .eq(PmsFarmerOriginDO::getOriginId, originId));
    }

    /**
     * 删除某个农户的所有产地关联
     */
    default int deleteByFarmerId(Long farmerId) {
        return delete(new LambdaQueryWrapper<PmsFarmerOriginDO>()
                .eq(PmsFarmerOriginDO::getFarmerId, farmerId));
    }

    /**
     * 删除某个产地的所有农户关联
     */
    default int deleteByOriginId(Long originId) {
        return delete(new LambdaQueryWrapper<PmsFarmerOriginDO>()
                .eq(PmsFarmerOriginDO::getOriginId, originId));
    }
}
