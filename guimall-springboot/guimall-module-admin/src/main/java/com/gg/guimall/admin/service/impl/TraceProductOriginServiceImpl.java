package com.gg.guimall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gg.guimall.admin.model.vo.trace.BindProductOriginReqVO;
import com.gg.guimall.admin.model.vo.trace.FindProductOriginRspVO;
import com.gg.guimall.admin.service.TraceProductOriginService;
import com.gg.guimall.common.domain.dos.PmsFarmerDO;
import com.gg.guimall.common.domain.dos.PmsProductDO;
import com.gg.guimall.common.domain.dos.TraceOriginDO;
import com.gg.guimall.common.domain.dos.TraceProductOriginDO;
import com.gg.guimall.common.domain.mapper.PmsFarmerMapper;
import com.gg.guimall.common.domain.mapper.PmsProductMapper;
import com.gg.guimall.common.domain.mapper.TraceOriginMapper;
import com.gg.guimall.common.domain.mapper.TraceProductOriginMapper;
import com.gg.guimall.common.enums.ResponseCodeEnum;
import com.gg.guimall.common.exception.BizException;
import com.gg.guimall.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 商品产地关联 Service 实现类（基于 trace_product_origin 表）
 *
 * @author wly
 * @url www.gg.com
 * @date 2026/3/19
 */
@Service
@Slf4j
public class TraceProductOriginServiceImpl implements TraceProductOriginService {

    @Autowired
    private TraceProductOriginMapper traceProductOriginMapper;

    @Autowired
    private TraceOriginMapper traceOriginMapper;

    @Autowired
    private PmsProductMapper pmsProductMapper;

    @Autowired
    private PmsFarmerMapper pmsFarmerMapper;

    @Override
    public Response bind(BindProductOriginReqVO reqVO) {

        if (Objects.isNull(reqVO.getProductId()) || reqVO.getProductId() <= 0
                || Objects.isNull(reqVO.getOriginId()) || reqVO.getOriginId() <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        PmsProductDO productDO = pmsProductMapper.selectById(reqVO.getProductId());
        if (Objects.isNull(productDO)) {
            throw new BizException(ResponseCodeEnum.PRODUCT_NOT_FOUND);
        }

        TraceOriginDO originDO = traceOriginMapper.selectById(reqVO.getOriginId());
        if (Objects.isNull(originDO)) {
            throw new BizException(ResponseCodeEnum.ORIGIN_NOT_FOUND);
        }

        Long farmerId = Objects.nonNull(reqVO.getFarmerId()) ? reqVO.getFarmerId() : productDO.getFarmerId();
        if (Objects.nonNull(farmerId) && farmerId > 0) {
            PmsFarmerDO farmerDO = pmsFarmerMapper.selectById(farmerId);
            if (Objects.isNull(farmerDO)) {
                throw new BizException(ResponseCodeEnum.FARMER_NOT_FOUND);
            }
        }

        TraceProductOriginDO exist = traceProductOriginMapper.selectByProductId(reqVO.getProductId());
        if (Objects.nonNull(exist)) {
            TraceProductOriginDO update = TraceProductOriginDO.builder()
                    .id(exist.getId())
                    .productId(reqVO.getProductId())
                    .originId(reqVO.getOriginId())
                    .farmerId(farmerId)
                    .build();
            traceProductOriginMapper.updateById(update);
            return Response.success();
        }

        TraceProductOriginDO insert = TraceProductOriginDO.builder()
                .productId(reqVO.getProductId())
                .originId(reqVO.getOriginId())
                .farmerId(farmerId)
                .build();
        traceProductOriginMapper.insert(insert);

        return Response.success();
    }

    @Override
    public Response findByProductId(Long productId) {

        if (Objects.isNull(productId) || productId <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        TraceProductOriginDO relation = traceProductOriginMapper.selectByProductId(productId);
        if (Objects.isNull(relation)) {
            throw new BizException(ResponseCodeEnum.PRODUCT_ORIGIN_NOT_FOUND);
        }

        TraceOriginDO originDO = traceOriginMapper.selectById(relation.getOriginId());
        PmsFarmerDO farmerDO = Objects.nonNull(relation.getFarmerId())
                ? pmsFarmerMapper.selectById(relation.getFarmerId())
                : null;

        FindProductOriginRspVO rspVO = new FindProductOriginRspVO();
        rspVO.setId(relation.getId());
        rspVO.setProductId(relation.getProductId());
        rspVO.setOriginId(relation.getOriginId());
        rspVO.setFarmerId(relation.getFarmerId());
        rspVO.setCreateTime(relation.getCreateTime());
        rspVO.setOriginName(Objects.nonNull(originDO) ? originDO.getOriginName() : null);
        rspVO.setFarmerName(Objects.nonNull(farmerDO) ? farmerDO.getName() : null);

        return Response.success(rspVO);
    }

    @Override
    public Response unbind(Long productId) {

        if (Objects.isNull(productId) || productId <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        traceProductOriginMapper.delete(new LambdaQueryWrapper<TraceProductOriginDO>()
                .eq(TraceProductOriginDO::getProductId, productId));

        return Response.success();
    }
}

