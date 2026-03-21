package com.gg.guimall.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.common.domain.dos.SmsHomeAdvertiseDO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 首页轮播广告 Mapper
 *
 * 对应表：sms_home_advertise
 */
public interface SmsHomeAdvertiseMapper extends BaseMapper<SmsHomeAdvertiseDO> {

    /**
     * 后台分页查询
     */
    default Page<SmsHomeAdvertiseDO> selectPageList(
            long current,
            long size,
            String name,
            Integer type,
            Integer status,
            LocalDateTime beginTime,
            LocalDateTime endTime
    ) {
        Page<SmsHomeAdvertiseDO> page = new Page<>(current, size);
        LambdaQueryWrapper<SmsHomeAdvertiseDO> wrapper = new LambdaQueryWrapper<>();

        wrapper
                .like(Objects.nonNull(name) && name.trim().length() > 0,
                        SmsHomeAdvertiseDO::getName, name)
                .eq(Objects.nonNull(type), SmsHomeAdvertiseDO::getType, type)
                .eq(Objects.nonNull(status), SmsHomeAdvertiseDO::getStatus, status)
                // 以活动区间过滤：startTime >= beginTime 且 endTime <= endTime
                .ge(Objects.nonNull(beginTime), SmsHomeAdvertiseDO::getStartTime, beginTime)
                .le(Objects.nonNull(endTime), SmsHomeAdvertiseDO::getEndTime, endTime)
                .orderByDesc(SmsHomeAdvertiseDO::getCreateTime);

        return selectPage(page, wrapper);
    }

    /**
     * 前台获取轮播广告（仅上线且在时间范围内）
     */
    default List<SmsHomeAdvertiseDO> selectActiveList(Integer type, LocalDateTime now) {
        LambdaQueryWrapper<SmsHomeAdvertiseDO> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .eq(SmsHomeAdvertiseDO::getStatus, 1)
                .eq(SmsHomeAdvertiseDO::getType, type)
                // 时间校验：now 在 [startTime, endTime]
                .le(SmsHomeAdvertiseDO::getStartTime, now)
                .ge(SmsHomeAdvertiseDO::getEndTime, now)
                .orderByAsc(SmsHomeAdvertiseDO::getSort)
                .orderByDesc(SmsHomeAdvertiseDO::getId);
        return selectList(wrapper);
    }
}

