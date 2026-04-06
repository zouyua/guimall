package com.gg.guimall.admin.service.impl;

import com.gg.guimall.admin.model.vo.trace.CreateTraceRecordReqVO;
import com.gg.guimall.admin.model.vo.trace.FindTraceRecordListReqVO;
import com.gg.guimall.admin.model.vo.trace.TraceRecordRspVO;
import com.gg.guimall.admin.model.vo.trace.UpdateTraceRecordReqVO;
import com.gg.guimall.admin.service.TraceRecordService;
import com.gg.guimall.common.domain.dos.PmsFarmerDO;
import com.gg.guimall.common.domain.dos.PmsProductDO;
import com.gg.guimall.common.domain.dos.TraceRecordDO;
import com.gg.guimall.common.domain.dos.TraceRecordTypeDO;
import com.gg.guimall.common.domain.mapper.PmsFarmerMapper;
import com.gg.guimall.common.domain.mapper.PmsProductMapper;
import com.gg.guimall.common.domain.mapper.TraceRecordMapper;
import com.gg.guimall.common.domain.mapper.TraceRecordTypeMapper;
import com.gg.guimall.common.enums.ResponseCodeEnum;
import com.gg.guimall.common.exception.BizException;
import com.gg.guimall.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 溯源记录 Service 实现类（基于 trace_record 表）
 *
 * @author wly
 * @url www.gg.com
 * @date 2026/3/19
 */
@Service
@Slf4j
public class TraceRecordServiceImpl implements TraceRecordService {

    @Autowired
    private TraceRecordMapper traceRecordMapper;

    @Autowired
    private PmsProductMapper pmsProductMapper;

    @Autowired
    private PmsFarmerMapper pmsFarmerMapper;

    @Autowired
    private TraceRecordTypeMapper traceRecordTypeMapper;

    @Override
    public Response create(CreateTraceRecordReqVO reqVO) {

        PmsProductDO productDO = pmsProductMapper.selectById(reqVO.getProductId());
        if (Objects.isNull(productDO)) {
            throw new BizException(ResponseCodeEnum.PRODUCT_NOT_FOUND);
        }

        Long farmerId = Objects.nonNull(reqVO.getFarmerId()) ? reqVO.getFarmerId() : productDO.getFarmerId();
        if (Objects.nonNull(farmerId) && farmerId > 0) {
            PmsFarmerDO farmerDO = pmsFarmerMapper.selectById(farmerId);
            if (Objects.isNull(farmerDO)) {
                throw new BizException(ResponseCodeEnum.FARMER_NOT_FOUND);
            }
        }

        TraceRecordDO recordDO = TraceRecordDO.builder()
                .productId(reqVO.getProductId())
                .farmerId(farmerId)
                .recordTypeId(reqVO.getRecordTypeId())
                .content(reqVO.getContent())
                .recordTime(Objects.nonNull(reqVO.getRecordTime()) ? reqVO.getRecordTime() : LocalDateTime.now())
                .pic(reqVO.getPic())
                .build();

        traceRecordMapper.insert(recordDO);
        return Response.success();
    }

    @Override
    public Response update(UpdateTraceRecordReqVO reqVO) {

        TraceRecordDO exist = traceRecordMapper.selectById(reqVO.getId());
        if (Objects.isNull(exist)) {
            throw new BizException(ResponseCodeEnum.TRACE_RECORD_NOT_FOUND);
        }

        PmsProductDO productDO = pmsProductMapper.selectById(reqVO.getProductId());
        if (Objects.isNull(productDO)) {
            throw new BizException(ResponseCodeEnum.PRODUCT_NOT_FOUND);
        }

        Long farmerId = Objects.nonNull(reqVO.getFarmerId()) ? reqVO.getFarmerId() : productDO.getFarmerId();
        if (Objects.nonNull(farmerId) && farmerId > 0) {
            PmsFarmerDO farmerDO = pmsFarmerMapper.selectById(farmerId);
            if (Objects.isNull(farmerDO)) {
                throw new BizException(ResponseCodeEnum.FARMER_NOT_FOUND);
            }
        }

        TraceRecordDO update = TraceRecordDO.builder()
                .id(reqVO.getId())
                .productId(reqVO.getProductId())
                .farmerId(farmerId)
                .recordTypeId(reqVO.getRecordTypeId())
                .content(reqVO.getContent())
                .recordTime(reqVO.getRecordTime())
                .pic(reqVO.getPic())
                .build();

        traceRecordMapper.updateById(update);
        return Response.success();
    }

    @Override
    public Response delete(Long id) {

        if (Objects.isNull(id) || id <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        TraceRecordDO exist = traceRecordMapper.selectById(id);
        if (Objects.isNull(exist)) {
            throw new BizException(ResponseCodeEnum.TRACE_RECORD_NOT_FOUND);
        }

        traceRecordMapper.deleteById(id);
        return Response.success();
    }

    @Override
    public Response list(FindTraceRecordListReqVO reqVO) {

        PmsProductDO productDO = pmsProductMapper.selectById(reqVO.getProductId());
        if (Objects.isNull(productDO)) {
            throw new BizException(ResponseCodeEnum.PRODUCT_NOT_FOUND);
        }

        List<TraceRecordDO> list = traceRecordMapper.selectByProductId(reqVO.getProductId());

        // 批量查询所有记录类型，用于翻译 recordTypeId → typeName
        List<Long> typeIds = list.stream()
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

        Map<Long, String> finalTypeNameMap = typeNameMap;
        List<TraceRecordRspVO> rspList = list.stream()
                .map(r -> {
                    TraceRecordRspVO vo = new TraceRecordRspVO();
                    BeanUtils.copyProperties(r, vo);
                    vo.setRecordTypeName(finalTypeNameMap.getOrDefault(r.getRecordTypeId(), "未知类型"));
                    return vo;
                })
                .collect(Collectors.toList());

        return Response.success(rspList);
    }
}

