package com.gg.guimall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.admin.model.vo.farmer.*;
import com.gg.guimall.admin.service.FarmerService;
import com.gg.guimall.common.domain.dos.PmsFarmerDO;
import com.gg.guimall.common.domain.mapper.PmsFarmerMapper;
import com.gg.guimall.common.enums.ResponseCodeEnum;
import com.gg.guimall.common.exception.BizException;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
     * 创建农户
     */
    @Override
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
        return Response.success();
    }

    /**
     * 分页查询农户列表
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
        return PageResponse.success(resultPage);
    }

    /**
     * 查询农户详情（对应 pms_farmer，无产地ID；产地通过 trace_product_origin 等关联）
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
        return Response.success(rspVO);
    }

    /**
     * 修改农户信息
     */
    @Override
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
        return Response.success();
    }

    /**
     * 删除农户
     */
    @Override
    public Response deleteFarmer(Long id) {

        if (Objects.isNull(id) || id <= 0) {
            throw new BizException(ResponseCodeEnum.INVALID_PRODUCT_DATA);
        }

        PmsFarmerDO farmerDO = pmsFarmerMapper.selectById(id);
        if (Objects.isNull(farmerDO)) {
            throw new BizException(ResponseCodeEnum.FARMER_NOT_FOUND);
        }

        pmsFarmerMapper.deleteById(id);
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
}
