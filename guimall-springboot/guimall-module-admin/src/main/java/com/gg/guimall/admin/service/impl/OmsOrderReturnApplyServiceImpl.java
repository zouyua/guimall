package com.gg.guimall.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.admin.model.vo.oms.*;
import com.gg.guimall.admin.service.OmsOrderReturnApplyService;
import com.gg.guimall.common.domain.dos.OmsCompanyAddressDO;
import com.gg.guimall.common.domain.dos.OmsOrderReturnApplyDO;
import com.gg.guimall.common.domain.mapper.OmsCompanyAddressMapper;
import com.gg.guimall.common.domain.mapper.OmsOrderReturnApplyMapper;
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
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 * @description: 退货申请 Service 实现类
 **/
@Service
@Slf4j
public class OmsOrderReturnApplyServiceImpl implements OmsOrderReturnApplyService {

    @Autowired
    private OmsOrderReturnApplyMapper returnApplyMapper;

    @Autowired
    private OmsCompanyAddressMapper companyAddressMapper;

    @Override
    public PageResponse<FindOmsOrderReturnApplyPageRspVO> findReturnApplyPageList(FindOmsOrderReturnApplyPageReqVO reqVO) {
        Page<OmsOrderReturnApplyDO> page = returnApplyMapper.selectPageList(
                reqVO.getCurrent(),
                reqVO.getSize(),
                reqVO.getOrderId(),
                reqVO.getOrderSn(),
                reqVO.getMemberUsername(),
                reqVO.getStatus()
        );

        List<FindOmsOrderReturnApplyPageRspVO> voList = page.getRecords().stream()
                .map(item -> {
                    FindOmsOrderReturnApplyPageRspVO vo = new FindOmsOrderReturnApplyPageRspVO();
                    BeanUtils.copyProperties(item, vo);
                    return vo;
                })
                .collect(Collectors.toList());

        return PageResponse.success(page, voList);
    }

    @Override
    public Response findReturnApplyDetail(Long id) {
        if (Objects.isNull(id) || id <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        OmsOrderReturnApplyDO applyDO = returnApplyMapper.selectById(id);
        if (Objects.isNull(applyDO)) {
            throw new BizException(ResponseCodeEnum.ORDER_RETURN_NOT_FOUND);
        }

        FindOmsOrderReturnApplyDetailRspVO rspVO = new FindOmsOrderReturnApplyDetailRspVO();
        BeanUtils.copyProperties(applyDO, rspVO);

        if (Objects.nonNull(applyDO.getCompanyAddressId())) {
            OmsCompanyAddressDO addressDO = companyAddressMapper.selectById(applyDO.getCompanyAddressId());
            if (Objects.nonNull(addressDO)) {
                FindOmsOrderReturnApplyDetailRspVO.CompanyAddress address = new FindOmsOrderReturnApplyDetailRspVO.CompanyAddress();
                BeanUtils.copyProperties(addressDO, address);
                rspVO.setCompanyAddress(address);
            }
        }

        return Response.success(rspVO);
    }

    @Override
    public Response updateReturnApplyStatus(UpdateOmsOrderReturnApplyStatusReqVO reqVO) {
        OmsOrderReturnApplyDO applyDO = returnApplyMapper.selectById(reqVO.getId());
        if (Objects.isNull(applyDO)) {
            throw new BizException(ResponseCodeEnum.ORDER_RETURN_NOT_FOUND);
        }

        Integer fromStatus = applyDO.getStatus();
        Integer toStatus = reqVO.getStatus();

        if (!isValidStatusTransition(fromStatus, toStatus)) {
            throw new BizException(ResponseCodeEnum.ORDER_RETURN_STATUS_ILLEGAL);
        }

        OmsOrderReturnApplyDO updateDO = OmsOrderReturnApplyDO.builder()
                .id(applyDO.getId())
                .status(toStatus)
                .build();
        returnApplyMapper.updateById(updateDO);
        return Response.success();
    }

    private boolean isValidStatusTransition(Integer fromStatus, Integer toStatus) {
        if (fromStatus == null || toStatus == null) {
            return false;
        }
        // 0->1->2 或 0/1->3
        if (fromStatus == 0) {
            return toStatus == 1 || toStatus == 3;
        }
        if (fromStatus == 1) {
            return toStatus == 2 || toStatus == 3;
        }
        // 2 已完成 / 3 已拒绝 不允许再修改
        return false;
    }
}

