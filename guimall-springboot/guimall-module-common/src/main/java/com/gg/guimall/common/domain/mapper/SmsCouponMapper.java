package com.gg.guimall.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.common.domain.dos.SmsCouponDO;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 * @description: 优惠券 Mapper
 **/
public interface SmsCouponMapper extends BaseMapper<SmsCouponDO> {

    default Page<SmsCouponDO> selectPageList(
            long current,
            long size,
            String name,
            Integer type,
            Integer useType,
            Integer platform,
            LocalDateTime beginTime,
            LocalDateTime endTime
    ) {
        Page<SmsCouponDO> page = new Page<>(current, size);

        LambdaQueryWrapper<SmsCouponDO> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .like(Objects.nonNull(name) && name.trim().length() > 0, SmsCouponDO::getName, name)
                .eq(Objects.nonNull(type), SmsCouponDO::getType, type)
                .eq(Objects.nonNull(useType), SmsCouponDO::getUseType, useType)
                .eq(Objects.nonNull(platform), SmsCouponDO::getPlatform, platform)
                // "开始时间/结束时间"按优惠券有效期过滤
                .ge(Objects.nonNull(beginTime), SmsCouponDO::getStartTime, beginTime)
                .le(Objects.nonNull(endTime), SmsCouponDO::getEndTime, endTime)
                .orderByDesc(SmsCouponDO::getCreateTime);

        return selectPage(page, wrapper);
    }
}

