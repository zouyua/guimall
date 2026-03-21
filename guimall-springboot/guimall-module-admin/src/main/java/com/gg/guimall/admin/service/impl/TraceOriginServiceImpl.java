package com.gg.guimall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.admin.model.vo.trace.CreateOriginReqVO;
import com.gg.guimall.admin.model.vo.trace.FindOriginDetailRspVO;
import com.gg.guimall.admin.model.vo.trace.FindOriginPageListReqVO;
import com.gg.guimall.admin.model.vo.trace.OriginOptionVO;
import com.gg.guimall.admin.model.vo.trace.UpdateOriginReqVO;
import com.gg.guimall.admin.service.TraceOriginService;
import com.gg.guimall.common.domain.dos.TraceOriginDO;
import com.gg.guimall.common.domain.mapper.TraceOriginMapper;
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
 * 产地管理 Service 实现类（基于 trace_origin 表）
 *
 * @author wly
 * @url www.gg.com
 * @date 2026/3/19
 */
@Service
@Slf4j
public class TraceOriginServiceImpl implements TraceOriginService {

    @Autowired
    private TraceOriginMapper traceOriginMapper;

    @Override
    public Response createOrigin(CreateOriginReqVO createOriginReqVO) {

        TraceOriginDO exist = traceOriginMapper.selectOne(new LambdaQueryWrapper<TraceOriginDO>()
                .eq(TraceOriginDO::getOriginName, createOriginReqVO.getOriginName()));
        if (Objects.nonNull(exist)) {
            throw new BizException(ResponseCodeEnum.ORIGIN_NAME_EXISTS);
        }

        TraceOriginDO originDO = TraceOriginDO.builder()
                .originName(createOriginReqVO.getOriginName())
                .province(createOriginReqVO.getProvince())
                .city(createOriginReqVO.getCity())
                .region(createOriginReqVO.getRegion())
                .description(createOriginReqVO.getDescription())
                .build();

        traceOriginMapper.insert(originDO);
        return Response.success();
    }

    @Override
    public PageResponse findOriginPageList(FindOriginPageListReqVO reqVO) {

        Page<TraceOriginDO> page = traceOriginMapper.selectPageList(
                reqVO.getCurrent(),
                reqVO.getSize(),
                reqVO.getOriginName(),
                reqVO.getProvince(),
                reqVO.getCity(),
                reqVO.getRegion()
        );

        return PageResponse.success(page);
    }

    @Override
    public Response findOriginDetail(Long id) {

        if (Objects.isNull(id) || id <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        TraceOriginDO originDO = traceOriginMapper.selectById(id);
        if (Objects.isNull(originDO)) {
            throw new BizException(ResponseCodeEnum.ORIGIN_NOT_FOUND);
        }

        FindOriginDetailRspVO rspVO = new FindOriginDetailRspVO();
        BeanUtils.copyProperties(originDO, rspVO);
        return Response.success(rspVO);
    }

    @Override
    public Response updateOrigin(UpdateOriginReqVO reqVO) {

        if (Objects.isNull(reqVO.getId()) || reqVO.getId() <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        TraceOriginDO exist = traceOriginMapper.selectById(reqVO.getId());
        if (Objects.isNull(exist)) {
            throw new BizException(ResponseCodeEnum.ORIGIN_NOT_FOUND);
        }

        TraceOriginDO duplicate = traceOriginMapper.selectOne(new LambdaQueryWrapper<TraceOriginDO>()
                .eq(TraceOriginDO::getOriginName, reqVO.getOriginName())
                .ne(TraceOriginDO::getId, reqVO.getId()));
        if (Objects.nonNull(duplicate)) {
            throw new BizException(ResponseCodeEnum.ORIGIN_NAME_EXISTS);
        }

        TraceOriginDO updateDO = TraceOriginDO.builder()
                .id(reqVO.getId())
                .originName(reqVO.getOriginName())
                .province(reqVO.getProvince())
                .city(reqVO.getCity())
                .region(reqVO.getRegion())
                .description(reqVO.getDescription())
                .build();

        traceOriginMapper.updateById(updateDO);
        return Response.success();
    }

    @Override
    public Response deleteOrigin(Long id) {

        if (Objects.isNull(id) || id <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        TraceOriginDO originDO = traceOriginMapper.selectById(id);
        if (Objects.isNull(originDO)) {
            throw new BizException(ResponseCodeEnum.ORIGIN_NOT_FOUND);
        }

        traceOriginMapper.deleteById(id);
        return Response.success();
    }

    @Override
    public Response findOriginOptions() {

        List<TraceOriginDO> list = traceOriginMapper.selectList(new LambdaQueryWrapper<TraceOriginDO>()
                .orderByAsc(TraceOriginDO::getOriginName));

        List<OriginOptionVO> options = list.stream()
                .map(o -> OriginOptionVO.builder()
                        .id(o.getId())
                        .originName(o.getOriginName())
                        .build())
                .collect(Collectors.toList());

        return Response.success(options);
    }
}

