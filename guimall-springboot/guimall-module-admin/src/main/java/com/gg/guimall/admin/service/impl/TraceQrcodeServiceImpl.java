package com.gg.guimall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gg.guimall.admin.model.vo.trace.FindTraceQrcodeRspVO;
import com.gg.guimall.admin.model.vo.trace.UpsertTraceQrcodeReqVO;
import com.gg.guimall.admin.service.TraceQrcodeService;
import com.gg.guimall.common.domain.dos.PmsProductDO;
import com.gg.guimall.common.domain.dos.TraceQrcodeDO;
import com.gg.guimall.common.domain.mapper.PmsProductMapper;
import com.gg.guimall.common.domain.mapper.TraceQrcodeMapper;
import com.gg.guimall.common.enums.ResponseCodeEnum;
import com.gg.guimall.common.exception.BizException;
import com.gg.guimall.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 溯源二维码 Service 实现类（基于 trace_qrcode 表）
 *
 * @author wly
 * @url www.gg.com
 * @date 2026/3/19
 */
@Service
@Slf4j
public class TraceQrcodeServiceImpl implements TraceQrcodeService {

    @Autowired
    private TraceQrcodeMapper traceQrcodeMapper;

    @Autowired
    private PmsProductMapper pmsProductMapper;

    @Override
    public Response upsert(UpsertTraceQrcodeReqVO reqVO) {

        PmsProductDO productDO = pmsProductMapper.selectById(reqVO.getProductId());
        if (Objects.isNull(productDO)) {
            throw new BizException(ResponseCodeEnum.PRODUCT_NOT_FOUND);
        }

        TraceQrcodeDO exist = traceQrcodeMapper.selectByProductId(reqVO.getProductId());
        if (Objects.nonNull(exist)) {
            TraceQrcodeDO update = TraceQrcodeDO.builder()
                    .id(exist.getId())
                    .productId(reqVO.getProductId())
                    .qrcodeUrl(reqVO.getQrcodeUrl())
                    .traceUrl(reqVO.getTraceUrl())
                    .build();
            traceQrcodeMapper.updateById(update);
            return Response.success();
        }

        TraceQrcodeDO insert = TraceQrcodeDO.builder()
                .productId(reqVO.getProductId())
                .qrcodeUrl(reqVO.getQrcodeUrl())
                .traceUrl(reqVO.getTraceUrl())
                .scanCount(0)
                .build();
        traceQrcodeMapper.insert(insert);
        return Response.success();
    }

    @Override
    public Response findByProductId(Long productId) {

        if (Objects.isNull(productId) || productId <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        TraceQrcodeDO qrcodeDO = traceQrcodeMapper.selectByProductId(productId);
        if (Objects.isNull(qrcodeDO)) {
            throw new BizException(ResponseCodeEnum.TRACE_QRCODE_NOT_FOUND);
        }

        FindTraceQrcodeRspVO rspVO = new FindTraceQrcodeRspVO();
        BeanUtils.copyProperties(qrcodeDO, rspVO);
        return Response.success(rspVO);
    }

    @Override
    public Response deleteByProductId(Long productId) {

        if (Objects.isNull(productId) || productId <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        traceQrcodeMapper.delete(new LambdaQueryWrapper<TraceQrcodeDO>()
                .eq(TraceQrcodeDO::getProductId, productId));
        return Response.success();
    }
}

