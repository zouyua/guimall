package com.gg.guimall.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.common.domain.dos.TraceOriginDO;

import java.util.Objects;

/**
 * 产地表 Mapper trace_origin
 *
 * @author wly
 * @url www.gg.com
 * @date 2026/3/19
 */
public interface TraceOriginMapper extends BaseMapper<TraceOriginDO> {

    default Page<TraceOriginDO> selectPageList(
            long current,
            long size,
            String originName,
            String province,
            String city,
            String region
    ) {

        Page<TraceOriginDO> page = new Page<>(current, size);

        LambdaQueryWrapper<TraceOriginDO> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .like(Objects.nonNull(originName) && originName.trim().length() > 0, TraceOriginDO::getOriginName, originName)
                .like(Objects.nonNull(province) && province.trim().length() > 0, TraceOriginDO::getProvince, province)
                .like(Objects.nonNull(city) && city.trim().length() > 0, TraceOriginDO::getCity, city)
                .like(Objects.nonNull(region) && region.trim().length() > 0, TraceOriginDO::getRegion, region)
                .orderByDesc(TraceOriginDO::getCreateTime);

        return selectPage(page, wrapper);
    }
}
