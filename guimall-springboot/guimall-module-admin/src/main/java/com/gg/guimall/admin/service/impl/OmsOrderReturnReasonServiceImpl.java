package com.gg.guimall.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.admin.model.vo.oms.*;
import com.gg.guimall.admin.service.OmsOrderReturnReasonService;
import com.gg.guimall.common.domain.dos.OmsOrderReturnReasonDO;
import com.gg.guimall.common.domain.mapper.OmsOrderReturnReasonMapper;
import com.gg.guimall.common.enums.ResponseCodeEnum;
import com.gg.guimall.common.exception.BizException;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 * @description: 退货原因 Service 实现类
 **/
@Service
@Slf4j
public class OmsOrderReturnReasonServiceImpl implements OmsOrderReturnReasonService {

    @Autowired
    private OmsOrderReturnReasonMapper returnReasonMapper;

    @Override
    public PageResponse<FindOmsOrderReturnReasonPageRspVO> findReturnReasonPageList(FindOmsOrderReturnReasonPageReqVO reqVO) {
        Page<OmsOrderReturnReasonDO> page = returnReasonMapper.selectPageList(
                reqVO.getCurrent(),
                reqVO.getSize(),
                reqVO.getName(),
                reqVO.getStatus()
        );

        List<FindOmsOrderReturnReasonPageRspVO> voList = page.getRecords().stream()
                .map(item -> {
                    FindOmsOrderReturnReasonPageRspVO vo = new FindOmsOrderReturnReasonPageRspVO();
                    BeanUtils.copyProperties(item, vo);
                    return vo;
                })
                .collect(Collectors.toList());

        return PageResponse.success(page, voList);
    }

    @Override
    public Response findReturnReasonDetail(Long id) {
        if (Objects.isNull(id) || id <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }
        OmsOrderReturnReasonDO reasonDO = returnReasonMapper.selectById(id);
        if (Objects.isNull(reasonDO)) {
            throw new BizException(ResponseCodeEnum.ORDER_RETURN_REASON_NOT_FOUND);
        }
        return Response.success(reasonDO);
    }

    @Override
    public Response createReturnReason(CreateOmsOrderReturnReasonReqVO reqVO) {
        OmsOrderReturnReasonDO reasonDO = OmsOrderReturnReasonDO.builder()
                .name(reqVO.getName())
                .sort(Objects.nonNull(reqVO.getSort()) ? reqVO.getSort() : 0)
                .status(Objects.nonNull(reqVO.getStatus()) ? reqVO.getStatus() : 1)
                .build();
        returnReasonMapper.insert(reasonDO);
        return Response.success();
    }

    @Override
    public Response updateReturnReason(UpdateOmsOrderReturnReasonReqVO reqVO) {
        OmsOrderReturnReasonDO exist = returnReasonMapper.selectById(reqVO.getId());
        if (Objects.isNull(exist)) {
            throw new BizException(ResponseCodeEnum.ORDER_RETURN_REASON_NOT_FOUND);
        }

        OmsOrderReturnReasonDO updateDO = new OmsOrderReturnReasonDO();
        BeanUtils.copyProperties(reqVO, updateDO);
        returnReasonMapper.updateById(updateDO);
        return Response.success();
    }

    @Override
    public Response updateReturnReasonStatus(UpdateOmsOrderReturnReasonStatusReqVO reqVO) {
        OmsOrderReturnReasonDO exist = returnReasonMapper.selectById(reqVO.getId());
        if (Objects.isNull(exist)) {
            throw new BizException(ResponseCodeEnum.ORDER_RETURN_REASON_NOT_FOUND);
        }
        OmsOrderReturnReasonDO updateDO = OmsOrderReturnReasonDO.builder()
                .id(reqVO.getId())
                .status(reqVO.getStatus())
                .build();
        returnReasonMapper.updateById(updateDO);
        return Response.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response deleteReturnReasons(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }
        returnReasonMapper.deleteBatchIds(ids);
        return Response.success();
    }
}

