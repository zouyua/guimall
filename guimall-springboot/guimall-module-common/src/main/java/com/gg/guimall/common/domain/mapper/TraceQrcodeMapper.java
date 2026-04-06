package com.gg.guimall.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gg.guimall.common.domain.dos.TraceQrcodeDO;

import java.util.Objects;

/**
 * 溯源二维码 Mapper trace_qrcode
 *
 * @author wly
 * @url www.gg.com
 * @date 2026/3/19
 */
public interface TraceQrcodeMapper extends BaseMapper<TraceQrcodeDO> {

    default TraceQrcodeDO selectByProductId(Long productId) {
        if (Objects.isNull(productId) || productId <= 0) {
            return null;
        }
        return selectOne(new LambdaQueryWrapper<TraceQrcodeDO>()
                .eq(TraceQrcodeDO::getProductId, productId)
                .orderByDesc(TraceQrcodeDO::getCreateTime)
                .last("limit 1"));
    }

    /** 原子自增扫码次数，避免先查再改的并发问题 */
    default int incrementScanCount(Long id) {
        return update(null, new LambdaUpdateWrapper<TraceQrcodeDO>()
                .setSql("scan_count = scan_count + 1")
                .eq(TraceQrcodeDO::getId, id));
    }
}
