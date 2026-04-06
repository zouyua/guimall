package com.gg.guimall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.admin.model.vo.farmer.*;
import com.gg.guimall.admin.service.FarmerService;
import com.gg.guimall.common.domain.dos.PmsFarmerDO;
import com.gg.guimall.common.domain.dos.PmsFarmerOriginDO;
import com.gg.guimall.common.domain.dos.TraceOriginDO;
import com.gg.guimall.common.domain.mapper.PmsFarmerMapper;
import com.gg.guimall.common.domain.mapper.PmsFarmerOriginMapper;
import com.gg.guimall.common.domain.mapper.TraceOriginMapper;
import com.gg.guimall.common.enums.ResponseCodeEnum;
import com.gg.guimall.common.exception.BizException;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 农户管理 Service 实现类（基于 pms_farmer 表）
 *
 * @author wly
 * @url www.gg.com
 * @date 2026/3/12
 */
@Service
@Slf4j
public class FarmerServiceImpl implements FarmerService {

    @Autowired
    private PmsFarmerMapper pmsFarmerMapper;

    @Autowired
    private PmsFarmerOriginMapper pmsFarmerOriginMapper;

    @Autowired
    private TraceOriginMapper traceOriginMapper;

    /**
     * 创建农户
     */
    @Override
    @Transactional
    public Response createFarmer(CreateFarmerReqVO createFarmerReqVO) {

        // 检查农户姓名是否已存在
        PmsFarmerDO existFarmer = pmsFarmerMapper.selectOne(
                new LambdaQueryWrapper<PmsFarmerDO>()
                        .eq(PmsFarmerDO::getName, createFarmerReqVO.getName())
        );
        if (Objects.nonNull(existFarmer)) {
            throw new BizException(ResponseCodeEnum.FARMER_NAME_EXISTS);
        }

        PmsFarmerDO farmerDO = PmsFarmerDO.builder()
                .name(createFarmerReqVO.getName())
                .phone(createFarmerReqVO.getPhone())
                .idCard(createFarmerReqVO.getIdCard())
                .avatar(createFarmerReqVO.getAvatar())
                .farmName(createFarmerReqVO.getFarmName())
                .province(createFarmerReqVO.getProvince())
                .city(createFarmerReqVO.getCity())
                .region(createFarmerReqVO.getRegion())
                .detailAddress(createFarmerReqVO.getDetailAddress())
                .mainProduct(createFarmerReqVO.getMainProduct())
                .description(createFarmerReqVO.getDescription())
                .status(Objects.nonNull(createFarmerReqVO.getStatus()) ? createFarmerReqVO.getStatus() : 1)
                .build();

        pmsFarmerMapper.insert(farmerDO);

        // 保存农户-产地关联
        saveFarmerOrigins(farmerDO.getId(), createFarmerReqVO.getOriginIds());

        return Response.success();
    }

    /**
     * 分页查询农户列表（包含关联产地名称）
     */
    @Override
    public PageResponse findFarmerPageList(FindFarmerPageListReqVO findFarmerPageListReqVO) {

        Page<PmsFarmerDO> page = new Page<>(findFarmerPageListReqVO.getCurrent(), findFarmerPageListReqVO.getSize());

        LambdaQueryWrapper<PmsFarmerDO> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .like(Objects.nonNull(findFarmerPageListReqVO.getName()), PmsFarmerDO::getName, findFarmerPageListReqVO.getName())
                .like(Objects.nonNull(findFarmerPageListReqVO.getPhone()), PmsFarmerDO::getPhone, findFarmerPageListReqVO.getPhone())
                .eq(Objects.nonNull(findFarmerPageListReqVO.getStatus()), PmsFarmerDO::getStatus, findFarmerPageListReqVO.getStatus())
                .orderByDesc(PmsFarmerDO::getCreateTime);

        Page<PmsFarmerDO> resultPage = pmsFarmerMapper.selectPage(page, wrapper);

        // 批量查询所有农户的产地关联，避免 N+1
        List<PmsFarmerDO> farmers = resultPage.getRecords();
        if (CollectionUtils.isEmpty(farmers)) {
            return PageResponse.success(resultPage, Collections.emptyList());
        }

        List<Long> farmerIds = farmers.stream().map(PmsFarmerDO::getId).collect(Collectors.toList());

        // 一次性查出所有相关的 farmer-origin 关联
        List<PmsFarmerOriginDO> allRelations = pmsFarmerOriginMapper.selectList(
                new LambdaQueryWrapper<PmsFarmerOriginDO>()
                        .in(PmsFarmerOriginDO::getFarmerId, farmerIds)
        );

        // 收集所有 originId，批量查产地名称
        List<Long> allOriginIds = allRelations.stream()
                .map(PmsFarmerOriginDO::getOriginId)
                .distinct()
                .collect(Collectors.toList());

        java.util.Map<Long, String> originNameMap = new java.util.HashMap<>();
        if (!allOriginIds.isEmpty()) {
            List<TraceOriginDO> origins = traceOriginMapper.selectBatchIds(allOriginIds);
            origins.forEach(o -> originNameMap.put(o.getId(), o.getOriginName()));
        }

        // 按 farmerId 分组
        java.util.Map<Long, List<PmsFarmerOriginDO>> relationMap = allRelations.stream()
                .collect(Collectors.groupingBy(PmsFarmerOriginDO::getFarmerId));

        // 组装 VO
        List<FindFarmerPageListRspVO> voList = farmers.stream().map(farmer -> {
            FindFarmerPageListRspVO vo = FindFarmerPageListRspVO.builder()
                    .id(farmer.getId())
                    .name(farmer.getName())
                    .phone(farmer.getPhone())
                    .idCard(farmer.getIdCard())
                    .avatar(farmer.getAvatar())
                    .farmName(farmer.getFarmName())
                    .province(farmer.getProvince())
                    .city(farmer.getCity())
                    .region(farmer.getRegion())
                    .detailAddress(farmer.getDetailAddress())
                    .mainProduct(farmer.getMainProduct())
                    .description(farmer.getDescription())
                    .certType(farmer.getCertType())
                    .certDesc(farmer.getCertDesc())
                    .certPic(farmer.getCertPic())
                    .status(farmer.getStatus())
                    .createTime(farmer.getCreateTime())
                    .updateTime(farmer.getUpdateTime())
                    .build();

            List<PmsFarmerOriginDO> rels = relationMap.getOrDefault(farmer.getId(), Collections.emptyList());
            List<String> names = rels.stream()
                    .map(r -> originNameMap.getOrDefault(r.getOriginId(), ""))
                    .filter(n -> !n.isEmpty())
                    .collect(Collectors.toList());
            vo.setOriginNames(names);

            return vo;
        }).collect(Collectors.toList());

        return PageResponse.success(resultPage, voList);
    }

    /**
     * 查询农户详情，包含关联的产地信息
     */
    @Override
    public Response findFarmerDetail(Long id) {

        if (Objects.isNull(id) || id <= 0) {
            throw new BizException(ResponseCodeEnum.INVALID_PRODUCT_DATA);
        }

        PmsFarmerDO farmerDO = pmsFarmerMapper.selectById(id);
        if (Objects.isNull(farmerDO)) {
            throw new BizException(ResponseCodeEnum.FARMER_NOT_FOUND);
        }

        FindFarmerDetailRspVO rspVO = new FindFarmerDetailRspVO();
        BeanUtils.copyProperties(farmerDO, rspVO);

        // 查询关联的产地列表
        List<PmsFarmerOriginDO> farmerOrigins = pmsFarmerOriginMapper.selectByFarmerId(id);
        if (!CollectionUtils.isEmpty(farmerOrigins)) {
            List<Long> originIds = farmerOrigins.stream()
                    .map(PmsFarmerOriginDO::getOriginId)
                    .collect(Collectors.toList());
            rspVO.setOriginIds(originIds);

            List<TraceOriginDO> origins = traceOriginMapper.selectBatchIds(originIds);
            List<String> originNames = origins.stream()
                    .map(TraceOriginDO::getOriginName)
                    .collect(Collectors.toList());
            rspVO.setOriginNames(originNames);
        } else {
            rspVO.setOriginIds(Collections.emptyList());
            rspVO.setOriginNames(Collections.emptyList());
        }

        return Response.success(rspVO);
    }

    /**
     * 修改农户信息
     */
    @Override
    @Transactional
    public Response updateFarmer(UpdateFarmerReqVO updateFarmerReqVO) {

        if (Objects.isNull(updateFarmerReqVO.getId()) || updateFarmerReqVO.getId() <= 0) {
            throw new BizException(ResponseCodeEnum.INVALID_PRODUCT_DATA);
        }

        PmsFarmerDO existFarmer = pmsFarmerMapper.selectById(updateFarmerReqVO.getId());
        if (Objects.isNull(existFarmer)) {
            throw new BizException(ResponseCodeEnum.FARMER_NOT_FOUND);
        }

        // 检查姓名是否被其他农户使用
        PmsFarmerDO duplicateName = pmsFarmerMapper.selectOne(
                new LambdaQueryWrapper<PmsFarmerDO>()
                        .eq(PmsFarmerDO::getName, updateFarmerReqVO.getName())
                        .ne(PmsFarmerDO::getId, updateFarmerReqVO.getId())
        );
        if (Objects.nonNull(duplicateName)) {
            throw new BizException(ResponseCodeEnum.FARMER_NAME_EXISTS);
        }

        PmsFarmerDO farmerDO = PmsFarmerDO.builder()
                .id(updateFarmerReqVO.getId())
                .name(updateFarmerReqVO.getName())
                .phone(updateFarmerReqVO.getPhone())
                .idCard(updateFarmerReqVO.getIdCard())
                .avatar(updateFarmerReqVO.getAvatar())
                .farmName(updateFarmerReqVO.getFarmName())
                .province(updateFarmerReqVO.getProvince())
                .city(updateFarmerReqVO.getCity())
                .region(updateFarmerReqVO.getRegion())
                .detailAddress(updateFarmerReqVO.getDetailAddress())
                .mainProduct(updateFarmerReqVO.getMainProduct())
                .description(updateFarmerReqVO.getDescription())
                .status(updateFarmerReqVO.getStatus())
                .build();

        pmsFarmerMapper.updateById(farmerDO);

        // 更新农户-产地关联（先删后插）
        saveFarmerOrigins(updateFarmerReqVO.getId(), updateFarmerReqVO.getOriginIds());

        return Response.success();
    }

    /**
     * 删除农户
     */
    @Override
    @Transactional
    public Response deleteFarmer(Long id) {

        if (Objects.isNull(id) || id <= 0) {
            throw new BizException(ResponseCodeEnum.INVALID_PRODUCT_DATA);
        }

        PmsFarmerDO farmerDO = pmsFarmerMapper.selectById(id);
        if (Objects.isNull(farmerDO)) {
            throw new BizException(ResponseCodeEnum.FARMER_NOT_FOUND);
        }

        pmsFarmerMapper.deleteById(id);
        // 同时删除农户-产地关联
        pmsFarmerOriginMapper.deleteByFarmerId(id);
        return Response.success();
    }

    /**
     * 获取农户下拉列表
     */
    @Override
    public Response findFarmerOptions() {

        List<PmsFarmerDO> farmerList = pmsFarmerMapper.selectList(
                new LambdaQueryWrapper<PmsFarmerDO>()
                        .eq(PmsFarmerDO::getStatus, 1)
                        .orderByAsc(PmsFarmerDO::getName)
        );

        List<FarmerOptionVO> options = farmerList.stream()
                .map(farmer -> FarmerOptionVO.builder()
                        .id(farmer.getId())
                        .name(farmer.getName())
                        .build())
                .collect(Collectors.toList());

        return Response.success(options);
    }

    /**
     * 保存农户-产地关联（先删后插）
     */
    private void saveFarmerOrigins(Long farmerId, List<Long> originIds) {
        // 先删除旧关联
        pmsFarmerOriginMapper.deleteByFarmerId(farmerId);

        // 再插入新关联
        if (!CollectionUtils.isEmpty(originIds)) {
            for (Long originId : originIds) {
                if (Objects.isNull(originId) || originId <= 0) continue;
                PmsFarmerOriginDO relation = PmsFarmerOriginDO.builder()
                        .farmerId(farmerId)
                        .originId(originId)
                        .build();
                pmsFarmerOriginMapper.insert(relation);
            }
        }
    }
}
