package com.gg.guimall.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.admin.model.vo.sms.CreateSmsHomeAdvertiseReqVO;
import com.gg.guimall.admin.model.vo.sms.FindSmsHomeAdvertiseDetailRspVO;
import com.gg.guimall.admin.model.vo.sms.FindSmsHomeAdvertisePageListReqVO;
import com.gg.guimall.admin.model.vo.sms.FindSmsHomeAdvertisePageListRspVO;
import com.gg.guimall.admin.model.vo.sms.UpdateSmsHomeAdvertiseReqVO;
import com.gg.guimall.admin.service.SmsHomeAdvertiseService;
import com.gg.guimall.common.domain.dos.SmsHomeAdvertiseDO;
import com.gg.guimall.common.domain.mapper.SmsHomeAdvertiseMapper;
import com.gg.guimall.common.enums.ResponseCodeEnum;
import com.gg.guimall.common.exception.BizException;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 首页轮播广告 Service 实现类
 */
@Service
@Slf4j
public class SmsHomeAdvertiseServiceImpl implements SmsHomeAdvertiseService {

    @Autowired
    private SmsHomeAdvertiseMapper smsHomeAdvertiseMapper;

    @Override
    public Response createAdvertise(CreateSmsHomeAdvertiseReqVO reqVO) {
        if (Objects.isNull(reqVO.getName()) ||
                Objects.isNull(reqVO.getType()) ||
                Objects.isNull(reqVO.getPic()) ||
                Objects.isNull(reqVO.getStartTime()) ||
                Objects.isNull(reqVO.getEndTime()) ||
                Objects.isNull(reqVO.getStatus()) ||
                Objects.isNull(reqVO.getSort())) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        if (reqVO.getType() < 0 || reqVO.getType() > 1) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }
        if (!Objects.equals(reqVO.getStatus(), 0) && !Objects.equals(reqVO.getStatus(), 1)) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }
        if (reqVO.getSort() < 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }
        if (reqVO.getEndTime().isBefore(reqVO.getStartTime())) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        SmsHomeAdvertiseDO advertiseDO = SmsHomeAdvertiseDO.builder()
                .name(reqVO.getName())
                .type(reqVO.getType())
                .pic(reqVO.getPic())
                .startTime(reqVO.getStartTime())
                .endTime(reqVO.getEndTime())
                .status(reqVO.getStatus())
                .url(reqVO.getUrl())
                .note(reqVO.getNote())
                .sort(reqVO.getSort())
                .clickCount(0)
                .orderCount(0)
                .build();

        smsHomeAdvertiseMapper.insert(advertiseDO);
        return Response.success();
    }

    @Override
    public PageResponse<FindSmsHomeAdvertisePageListRspVO> findAdvertisePageList(FindSmsHomeAdvertisePageListReqVO reqVO) {
        Page<SmsHomeAdvertiseDO> page = smsHomeAdvertiseMapper.selectPageList(
                reqVO.getCurrent(),
                reqVO.getSize(),
                reqVO.getName(),
                reqVO.getType(),
                reqVO.getStatus(),
                reqVO.getBeginTime(),
                reqVO.getEndTime()
        );

        List<FindSmsHomeAdvertisePageListRspVO> voList = page.getRecords().stream()
                .map(item -> {
                    FindSmsHomeAdvertisePageListRspVO vo = new FindSmsHomeAdvertisePageListRspVO();
                    BeanUtils.copyProperties(item, vo);
                    return vo;
                })
                .collect(Collectors.toList());

        return PageResponse.success(page, voList);
    }

    @Override
    public Response findAdvertiseDetail(Long id) {
        if (Objects.isNull(id) || id <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        SmsHomeAdvertiseDO advertiseDO = smsHomeAdvertiseMapper.selectById(id);
        if (Objects.isNull(advertiseDO)) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        FindSmsHomeAdvertiseDetailRspVO rspVO = new FindSmsHomeAdvertiseDetailRspVO();
        BeanUtils.copyProperties(advertiseDO, rspVO);
        return Response.success(rspVO);
    }

    @Override
    public Response updateAdvertise(UpdateSmsHomeAdvertiseReqVO reqVO) {
        if (Objects.isNull(reqVO.getId()) ||
                Objects.isNull(reqVO.getName()) ||
                Objects.isNull(reqVO.getType()) ||
                Objects.isNull(reqVO.getPic()) ||
                Objects.isNull(reqVO.getStartTime()) ||
                Objects.isNull(reqVO.getEndTime()) ||
                Objects.isNull(reqVO.getStatus()) ||
                Objects.isNull(reqVO.getSort())) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        if (reqVO.getId() <= 0 || reqVO.getType() < 0 || reqVO.getType() > 1) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }
        if (!Objects.equals(reqVO.getStatus(), 0) && !Objects.equals(reqVO.getStatus(), 1)) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }
        if (reqVO.getSort() < 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }
        if (reqVO.getEndTime().isBefore(reqVO.getStartTime())) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        SmsHomeAdvertiseDO exist = smsHomeAdvertiseMapper.selectById(reqVO.getId());
        if (Objects.isNull(exist)) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        SmsHomeAdvertiseDO updateDO = SmsHomeAdvertiseDO.builder()
                .id(reqVO.getId())
                .name(reqVO.getName())
                .type(reqVO.getType())
                .pic(reqVO.getPic())
                .startTime(reqVO.getStartTime())
                .endTime(reqVO.getEndTime())
                .status(reqVO.getStatus())
                .url(reqVO.getUrl())
                .note(reqVO.getNote())
                .sort(reqVO.getSort())
                .clickCount(exist.getClickCount())
                .orderCount(exist.getOrderCount())
                .createTime(exist.getCreateTime())
                .build();

        smsHomeAdvertiseMapper.updateById(updateDO);
        return Response.success();
    }

    @Override
    public Response deleteAdvertise(Long id) {
        if (Objects.isNull(id) || id <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        SmsHomeAdvertiseDO exist = smsHomeAdvertiseMapper.selectById(id);
        if (Objects.isNull(exist)) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        smsHomeAdvertiseMapper.deleteById(id);
        return Response.success();
    }
}

