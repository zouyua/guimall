package com.gg.guimall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gg.guimall.admin.model.vo.trace.FindFarmerBindingRspVO;
import com.gg.guimall.admin.model.vo.trace.FindOriginBindingRspVO;
import com.gg.guimall.admin.service.TraceProductOriginService;
import com.gg.guimall.common.domain.dos.PmsFarmerDO;
import com.gg.guimall.common.domain.dos.PmsFarmerOriginDO;
import com.gg.guimall.common.domain.dos.PmsProductDO;
import com.gg.guimall.common.domain.dos.TraceOriginDO;
import com.gg.guimall.common.domain.mapper.PmsFarmerMapper;
import com.gg.guimall.common.domain.mapper.PmsFarmerOriginMapper;
import com.gg.guimall.common.domain.mapper.PmsProductMapper;
import com.gg.guimall.common.domain.mapper.TraceOriginMapper;
import com.gg.guimall.common.enums.ResponseCodeEnum;
import com.gg.guimall.common.exception.BizException;
import com.gg.guimall.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 农户-产地关联查询 Service（基于 pms_farmer_origin + pms_product）
 * 用于删除拦截等场景
 *
 * @author wly
 * @date 2026/4/4
 */
@Service
@Slf4j
public class TraceProductOriginServiceImpl implements TraceProductOriginService {

    @Autowired
    private PmsFarmerOriginMapper pmsFarmerOriginMapper;

    @Autowired
    private TraceOriginMapper traceOriginMapper;

    @Autowired
    private PmsProductMapper pmsProductMapper;

    @Autowired
    private PmsFarmerMapper pmsFarmerMapper;

    /**
     * 根据产地ID查询关联的农户列表（用于删除产地前拦截）
     */
    @Override
    public Response findByOriginId(Long originId) {

        if (Objects.isNull(originId) || originId <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        TraceOriginDO originDO = traceOriginMapper.selectById(originId);
        if (Objects.isNull(originDO)) {
            throw new BizException(ResponseCodeEnum.ORIGIN_NOT_FOUND);
        }

        List<PmsFarmerOriginDO> farmerOrigins = pmsFarmerOriginMapper.selectByOriginId(originId);
        if (CollectionUtils.isEmpty(farmerOrigins)) {
            return Response.success(Collections.emptyList());
        }

        List<FindOriginBindingRspVO> rspList = new ArrayList<>();
        for (PmsFarmerOriginDO fo : farmerOrigins) {
            PmsFarmerDO farmerDO = pmsFarmerMapper.selectById(fo.getFarmerId());
            if (Objects.isNull(farmerDO)) continue;

            // 查询该农户名下的商品
            List<PmsProductDO> products = pmsProductMapper.selectList(
                    new LambdaQueryWrapper<PmsProductDO>()
                            .eq(PmsProductDO::getFarmerId, fo.getFarmerId()));

            if (CollectionUtils.isEmpty(products)) {
                // 该农户没有商品，也展示农户信息
                rspList.add(FindOriginBindingRspVO.builder()
                        .relationId(fo.getId())
                        .farmerId(farmerDO.getId())
                        .farmerName(farmerDO.getName())
                        .originId(originId)
                        .originName(originDO.getOriginName())
                        .createTime(fo.getCreateTime())
                        .build());
            } else {
                for (PmsProductDO product : products) {
                    rspList.add(FindOriginBindingRspVO.builder()
                            .relationId(fo.getId())
                            .productId(product.getId())
                            .productName(product.getName())
                            .farmerId(farmerDO.getId())
                            .farmerName(farmerDO.getName())
                            .originId(originId)
                            .originName(originDO.getOriginName())
                            .createTime(fo.getCreateTime())
                            .build());
                }
            }
        }

        return Response.success(rspList);
    }

    /**
     * 根据农户ID查询其名下的商品列表（用于删除农户前拦截）
     */
    @Override
    public Response findByFarmerId(Long farmerId) {

        if (Objects.isNull(farmerId) || farmerId <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        PmsFarmerDO farmerDO = pmsFarmerMapper.selectById(farmerId);
        if (Objects.isNull(farmerDO)) {
            throw new BizException(ResponseCodeEnum.FARMER_NOT_FOUND);
        }

        // 查询该农户名下的商品
        List<PmsProductDO> products = pmsProductMapper.selectList(
                new LambdaQueryWrapper<PmsProductDO>()
                        .eq(PmsProductDO::getFarmerId, farmerId));

        if (CollectionUtils.isEmpty(products)) {
            return Response.success(Collections.emptyList());
        }

        // 查询该农户关联的产地名称
        List<PmsFarmerOriginDO> farmerOrigins = pmsFarmerOriginMapper.selectByFarmerId(farmerId);
        String originNames = "";
        if (!CollectionUtils.isEmpty(farmerOrigins)) {
            List<Long> originIds = farmerOrigins.stream()
                    .map(PmsFarmerOriginDO::getOriginId)
                    .collect(Collectors.toList());
            List<TraceOriginDO> origins = traceOriginMapper.selectBatchIds(originIds);
            originNames = origins.stream()
                    .map(TraceOriginDO::getOriginName)
                    .collect(Collectors.joining("、"));
        }

        String finalOriginNames = originNames;
        List<FindFarmerBindingRspVO> rspList = products.stream().map(product ->
                FindFarmerBindingRspVO.builder()
                        .productId(product.getId())
                        .productName(product.getName())
                        .farmerId(farmerId)
                        .farmerName(farmerDO.getName())
                        .originName(finalOriginNames)
                        .build()
        ).collect(Collectors.toList());

        return Response.success(rspList);
    }
}
