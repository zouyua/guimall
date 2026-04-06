package com.gg.guimall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.admin.model.vo.oms.*;
import com.gg.guimall.admin.service.OmsOrderReturnApplyService;
import com.gg.guimall.common.domain.dos.OmsCompanyAddressDO;
import com.gg.guimall.common.domain.dos.OmsOrderDO;
import com.gg.guimall.common.domain.dos.OmsOrderReturnApplyDO;
import com.gg.guimall.common.domain.dos.SmsCouponHistoryDO;
import com.gg.guimall.common.domain.mapper.OmsCompanyAddressMapper;
import com.gg.guimall.common.domain.mapper.OmsOrderMapper;
import com.gg.guimall.common.domain.mapper.OmsOrderReturnApplyMapper;
import com.gg.guimall.common.domain.mapper.SmsCouponHistoryMapper;
import com.gg.guimall.common.domain.mapper.SmsCouponMapper;
import com.gg.guimall.common.enums.ResponseCodeEnum;
import com.gg.guimall.common.exception.BizException;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @Autowired
    private OmsOrderMapper orderMapper;

    @Autowired
    private SmsCouponHistoryMapper couponHistoryMapper;

    @Autowired
    private SmsCouponMapper couponMapper;

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
    @Transactional(rollbackFor = Exception.class)
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

        // 更新退货申请状态
        OmsOrderReturnApplyDO updateDO = OmsOrderReturnApplyDO.builder()
                .id(applyDO.getId())
                .status(toStatus)
                .build();
        returnApplyMapper.updateById(updateDO);

        // 如果退货申请通过（状态变为1-退货中），更新订单状态为已关闭（4）并恢复优惠券
        if (toStatus == 1 && Objects.nonNull(applyDO.getOrderId())) {
            OmsOrderDO orderDO = orderMapper.selectById(applyDO.getOrderId());
            if (Objects.nonNull(orderDO)) {
                orderDO.setStatus(4); // 已关闭
                orderDO.setUpdateTime(LocalDateTime.now());
                orderMapper.updateById(orderDO);
                log.info("退货申请通过，订单状态已更新为已关闭, orderId={}, orderSn={}", orderDO.getId(), orderDO.getOrderSn());

                // 恢复优惠券
                if (Objects.nonNull(orderDO.getCouponId()) && orderDO.getCouponId() > 0) {
                    LambdaQueryWrapper<SmsCouponHistoryDO> wrapper = Wrappers.lambdaQuery();
                    wrapper.eq(SmsCouponHistoryDO::getOrderId, orderDO.getId())
                            .eq(SmsCouponHistoryDO::getUseStatus, 1);
                    SmsCouponHistoryDO history = couponHistoryMapper.selectOne(wrapper);
                    if (Objects.nonNull(history)) {
                        history.setUseStatus(0);
                        history.setUseTime(null);
                        history.setOrderId(null);
                        history.setOrderSn(null);
                        couponHistoryMapper.updateById(history);

                        // 减少优惠券使用数量
                        couponMapper.decrementUseCount(orderDO.getCouponId());
                        log.info("退货申请通过，优惠券已恢复, couponId={}, orderId={}", orderDO.getCouponId(), orderDO.getId());
                    }
                }
            }
        }

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

