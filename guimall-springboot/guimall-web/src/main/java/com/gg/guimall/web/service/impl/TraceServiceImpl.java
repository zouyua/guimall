package com.gg.guimall.web.service.impl;

import com.gg.guimall.common.domain.dos.PmsFarmerDO;
import com.gg.guimall.common.domain.dos.PmsProductDO;
import com.gg.guimall.common.domain.dos.TraceOriginDO;
import com.gg.guimall.common.domain.dos.TraceProductOriginDO;
import com.gg.guimall.common.domain.dos.TraceQrcodeDO;
import com.gg.guimall.common.domain.dos.TraceRecordDO;
import com.gg.guimall.common.domain.mapper.PmsFarmerMapper;
import com.gg.guimall.common.domain.mapper.PmsProductMapper;
import com.gg.guimall.common.domain.mapper.TraceOriginMapper;
import com.gg.guimall.common.domain.mapper.TraceProductOriginMapper;
import com.gg.guimall.common.domain.mapper.TraceQrcodeMapper;
import com.gg.guimall.common.domain.mapper.TraceRecordMapper;
import com.gg.guimall.common.enums.ResponseCodeEnum;
import com.gg.guimall.common.exception.BizException;
import com.gg.guimall.common.utils.Response;
import com.gg.guimall.web.model.vo.trace.TraceDetailRspVO;
import com.gg.guimall.web.model.vo.trace.TraceRecordVO;
import com.gg.guimall.web.service.TraceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 溯源查询 Service 实现类
 *
 * @author wly
 * @url www.gg.com
 * @date 2026/3/19
 */
@Service
@Slf4j
public class TraceServiceImpl implements TraceService {

    @Autowired
    private PmsProductMapper pmsProductMapper;

    @Autowired
    private PmsFarmerMapper pmsFarmerMapper;

    @Autowired
    private TraceProductOriginMapper traceProductOriginMapper;

    @Autowired
    private TraceOriginMapper traceOriginMapper;

    @Autowired
    private TraceRecordMapper traceRecordMapper;

    @Autowired
    private TraceQrcodeMapper traceQrcodeMapper;

    @Override
    public Response findTraceDetail(Long productId) {

        if (Objects.isNull(productId) || productId <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        PmsProductDO productDO = pmsProductMapper.selectById(productId);
        if (Objects.isNull(productDO)) {
            throw new BizException(ResponseCodeEnum.PRODUCT_NOT_FOUND);
        }

        PmsFarmerDO farmerDO = Objects.nonNull(productDO.getFarmerId())
                ? pmsFarmerMapper.selectById(productDO.getFarmerId())
                : null;

        TraceProductOriginDO relation = traceProductOriginMapper.selectByProductId(productId);
        TraceOriginDO originDO = Objects.nonNull(relation) ? traceOriginMapper.selectById(relation.getOriginId()) : null;

        List<TraceRecordDO> recordList = traceRecordMapper.selectByProductId(productId);
        List<TraceRecordVO> records = recordList.stream()
                .map(r -> {
                    TraceRecordVO vo = new TraceRecordVO();
                    vo.setRecordType(r.getRecordType());
                    vo.setContent(r.getContent());
                    vo.setRecordTime(r.getRecordTime());
                    vo.setPic(r.getPic());
                    return vo;
                })
                .collect(Collectors.toList());

        TraceQrcodeDO qrcodeDO = traceQrcodeMapper.selectByProductId(productId);
        if (Objects.nonNull(qrcodeDO)) {
            // 原子自增，避免先查再改的并发问题
            traceQrcodeMapper.incrementScanCount(qrcodeDO.getId());
            qrcodeDO.setScanCount((qrcodeDO.getScanCount() == null ? 0 : qrcodeDO.getScanCount()) + 1);
        }

        TraceDetailRspVO rspVO = new TraceDetailRspVO();
        rspVO.setProductId(productDO.getId());
        rspVO.setProductName(productDO.getName());
        rspVO.setProductPic(productDO.getPic());

        rspVO.setFarmerId(productDO.getFarmerId());
        rspVO.setFarmerName(Objects.nonNull(farmerDO) ? farmerDO.getName() : null);
        rspVO.setFarmerPhone(Objects.nonNull(farmerDO) ? farmerDO.getPhone() : null);

        if (Objects.nonNull(originDO)) {
            rspVO.setOriginId(originDO.getId());
            rspVO.setOriginName(originDO.getOriginName());
            rspVO.setProvince(originDO.getProvince());
            rspVO.setCity(originDO.getCity());
            rspVO.setRegion(originDO.getRegion());
            rspVO.setOriginDescription(originDO.getDescription());
        }

        if (Objects.nonNull(qrcodeDO)) {
            rspVO.setTraceUrl(qrcodeDO.getTraceUrl());
            rspVO.setQrcodeUrl(qrcodeDO.getQrcodeUrl());
            rspVO.setScanCount(qrcodeDO.getScanCount());
        }

        rspVO.setRecords(records);
        return Response.success(rspVO);
    }
}

