package com.gg.guimall.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.common.domain.dos.UmsIntegrationHistoryDO;

import java.util.Objects;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/4/11
 * @description: 积分变动历史 Mapper
 **/
public interface UmsIntegrationHistoryMapper extends BaseMapper<UmsIntegrationHistoryDO> {

    /**
     * 分页查询积分变动历史
     */
    default Page<UmsIntegrationHistoryDO> selectPageList(
            long current,
            long size,
            Long memberId,
            Integer changeType
    ) {
        Page<UmsIntegrationHistoryDO> page = new Page<>(current, size);

        LambdaQueryWrapper<UmsIntegrationHistoryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .eq(Objects.nonNull(memberId), UmsIntegrationHistoryDO::getMemberId, memberId)
                .eq(Objects.nonNull(changeType), UmsIntegrationHistoryDO::getChangeType, changeType)
                .orderByDesc(UmsIntegrationHistoryDO::getCreateTime);

        return selectPage(page, wrapper);
    }
}
