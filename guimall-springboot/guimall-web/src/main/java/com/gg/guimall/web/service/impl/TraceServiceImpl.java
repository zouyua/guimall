package com.gg.guimall.web.service.impl;

import com.gg.guimall.common.domain.dos.PmsFarmerDO;
import com.gg.guimall.common.domain.dos.PmsFarmerOriginDO;
import com.gg.guimall.common.domain.dos.PmsProductDO;
import com.gg.guimall.common.domain.dos.TraceOriginDO;
import com.gg.guimall.common.domain.dos.TraceQrcodeDO;
import com.gg.guimall.common.domain.dos.TraceRecordDO;
import com.gg.guimall.common.domain.dos.TraceRecordTypeDO;
import com.gg.guimall.common.domain.mapper.PmsFarmerMapper;
import com.gg.guimall.common.domain.mapper.PmsFarmerOriginMapper;
import com.gg.guimall.common.domain.mapper.PmsProductMapper;
import com.gg.guimall.common.domain.mapper.TraceOriginMapper;
import com.gg.guimall.common.domain.mapper.TraceQrcodeMapper;
import com.gg.guimall.common.domain.mapper.TraceRecordMapper;
import com.gg.guimall.common.domain.mapper.TraceRecordTypeMapper;
import com.gg.guimall.common.enums.ResponseCodeEnum;
import com.gg.guimall.common.exception.BizException;
import com.gg.guimall.common.utils.Response;
import com.gg.guimall.web.model.vo.trace.TraceDetailRspVO;
import com.gg.guimall.web.model.vo.trace.TraceRecordVO;
import com.gg.guimall.web.service.TraceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private PmsFarmerOriginMapper pmsFarmerOriginMapper;

    @Autowired
    private TraceOriginMapper traceOriginMapper;

    @Autowired
    private TraceRecordMapper traceRecordMapper;

    @Autowired
    private TraceQrcodeMapper traceQrcodeMapper;

    @Autowired
    private TraceRecordTypeMapper traceRecordTypeMapper;

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

        // 通过 商品→农户→农户关联产地 链路获取产地信息
        TraceOriginDO originDO = null;
        if (Objects.nonNull(farmerDO)) {
            List<PmsFarmerOriginDO> farmerOrigins = pmsFarmerOriginMapper.selectByFarmerId(farmerDO.getId());
            if (!CollectionUtils.isEmpty(farmerOrigins)) {
                // 取第一个关联产地作为主产地
                originDO = traceOriginMapper.selectById(farmerOrigins.get(0).getOriginId());
            }
        }

        List<TraceRecordDO> recordList = traceRecordMapper.selectByProductId(productId);

        // 批量查询记录类型名称
        List<Long> typeIds = recordList.stream()
                .map(TraceRecordDO::getRecordTypeId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, String> typeNameMap = new HashMap<>();
        if (!typeIds.isEmpty()) {
            List<TraceRecordTypeDO> typeList = traceRecordTypeMapper.selectBatchIds(typeIds);
            typeNameMap = typeList.stream()
                    .collect(Collectors.toMap(TraceRecordTypeDO::getId, TraceRecordTypeDO::getTypeName));
        }

        // 批量查询记录中涉及的农户名称
        List<Long> farmerIds = recordList.stream()
                .map(TraceRecordDO::getFarmerId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, String> farmerNameMap = new HashMap<>();
        if (!farmerIds.isEmpty()) {
            List<PmsFarmerDO> farmers = pmsFarmerMapper.selectBatchIds(farmerIds);
            farmerNameMap = farmers.stream()
                    .collect(Collectors.toMap(PmsFarmerDO::getId, PmsFarmerDO::getName));
        }

        Map<Long, String> finalTypeNameMap = typeNameMap;
        Map<Long, String> finalFarmerNameMap = farmerNameMap;
        List<TraceRecordVO> records = recordList.stream()
                .map(r -> {
                    TraceRecordVO vo = new TraceRecordVO();
                    vo.setRecordTypeId(r.getRecordTypeId());
                    vo.setRecordTypeName(finalTypeNameMap.getOrDefault(r.getRecordTypeId(), "记录"));
                    vo.setContent(r.getContent());
                    vo.setRecordTime(r.getRecordTime());
                    vo.setPic(r.getPic());
                    vo.setFarmerName(Objects.nonNull(r.getFarmerId())
                            ? finalFarmerNameMap.getOrDefault(r.getFarmerId(), null) : null);
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
            rspVO.setAltitude(originDO.getAltitude());
            rspVO.setSunshineHours(originDO.getSunshineHours());
            rspVO.setSoilType(originDO.getSoilType());
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
