package com.gg.guimall.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.common.domain.dos.OmsOrderReturnReasonDO;

import java.util.Objects;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 * @description: 退货原因 Mapper
 **/
public interface OmsOrderReturnReasonMapper extends BaseMapper<OmsOrderReturnReasonDO> {

    default Page<OmsOrderReturnReasonDO> selectPageList(long current, long size, String name, Integer status) {
        Page<OmsOrderReturnReasonDO> page = new Page<>(current, size);

        LambdaQueryWrapper<OmsOrderReturnReasonDO> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .like(Objects.nonNull(name) && name.trim().length() > 0, OmsOrderReturnReasonDO::getName, name)
                .eq(Objects.nonNull(status), OmsOrderReturnReasonDO::getStatus, status)
                .orderByAsc(OmsOrderReturnReasonDO::getSort)
                .orderByDesc(OmsOrderReturnReasonDO::getCreateTime);

        return selectPage(page, wrapper);
    }
}

