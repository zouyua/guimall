package com.gg.guimall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.admin.model.vo.oms.*;
import com.gg.guimall.admin.model.vo.oms.FindOmsOrderPageListReqVO;
import com.gg.guimall.admin.model.vo.oms.FindOmsOrderPageListRspVO;
import com.gg.guimall.admin.service.OmsOrderService;
import com.gg.guimall.common.domain.dos.OmsOrderDO;
import com.gg.guimall.common.domain.dos.OmsOrderItemDO;
import com.gg.guimall.common.domain.dos.SmsCouponHistoryDO;
import com.gg.guimall.common.domain.mapper.OmsOrderItemMapper;
import com.gg.guimall.common.domain.mapper.OmsOrderMapper;
import com.gg.guimall.common.domain.mapper.PmsSkuStockMapper;
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
 * @description: 订单管理 Service 实现类
 **/
@Service("adminOmsOrderServiceImpl")
@Slf4j
public class OmsOrderServiceImpl implements OmsOrderService {

    @Autowired
    private OmsOrderMapper orderMapper;

    @Autowired
    private OmsOrderItemMapper orderItemMapper;

    @Autowired
    private PmsSkuStockMapper pmsSkuStockMapper;

    @Autowired
    private SmsCouponMapper smsCouponMapper;

    @Autowired
    private SmsCouponHistoryMapper smsCouponHistoryMapper;

    @Override
    public PageResponse<FindOmsOrderPageListRspVO> findOrderPageList(FindOmsOrderPageListReqVO reqVO) {

        Page<OmsOrderDO> page = orderMapper.selectPageList(
                reqVO.getCurrent(),
                reqVO.getSize(),
                reqVO.getOrderId(),
                reqVO.getOrderSn(),
                reqVO.getMemberId(),
                reqVO.getMemberUsername(),
                reqVO.getFarmerId(),
                reqVO.getStatus(),
                null,
                null
        );

        List<FindOmsOrderPageListRspVO> voList = page.getRecords().stream()
                .map(item -> {
                    FindOmsOrderPageListRspVO vo = new FindOmsOrderPageListRspVO();
                    BeanUtils.copyProperties(item, vo);
                    return vo;
                })
                .collect(Collectors.toList());

        return PageResponse.success(page, voList);
    }

    @Override
    public Response findOrderDetail(Long id) {
        if (Objects.isNull(id) || id <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        OmsOrderDO orderDO = orderMapper.selectById(id);
        if (Objects.isNull(orderDO) || Objects.equals(orderDO.getIsDeleted(), 1)) {
            throw new BizException(ResponseCodeEnum.ORDER_NOT_FOUND);
        }

        List<OmsOrderItemDO> itemDOList = orderItemMapper.selectByOrderId(id);
        List<FindOmsOrderDetailRspVO.OrderItem> items = itemDOList.stream()
                .map(itemDO -> FindOmsOrderDetailRspVO.OrderItem.builder()
                        .id(itemDO.getId())
                        .productId(itemDO.getProductId())
                        .productName(itemDO.getProductName())
                        .productPic(itemDO.getProductPic())
                        .productSkuId(itemDO.getProductSkuId())
                        .productSkuCode(itemDO.getProductSkuCode())
                        .productPrice(itemDO.getProductPrice())
                        .productQuantity(itemDO.getProductQuantity())
                        .realAmount(itemDO.getRealAmount())
                        .productAttr(itemDO.getProductAttr())
                        .build())
                .collect(Collectors.toList());

        FindOmsOrderDetailRspVO rspVO = new FindOmsOrderDetailRspVO();
        BeanUtils.copyProperties(orderDO, rspVO);
        rspVO.setItems(items);
        return Response.success(rspVO);
    }

    @Override
    public Response deliverOrder(DeliverOmsOrderReqVO reqVO) {
        OmsOrderDO orderDO = getValidOrder(reqVO.getId());
        // 仅“待发货(1)”允许发货 -> “已发货(2)”
        if (!Objects.equals(orderDO.getStatus(), 1)) {
            throw new BizException(ResponseCodeEnum.ORDER_STATUS_ILLEGAL);
        }

        OmsOrderDO updateDO = OmsOrderDO.builder()
                .id(orderDO.getId())
                .status(2)
                .deliveryCompany(reqVO.getDeliveryCompany())
                .deliverySn(reqVO.getDeliverySn())
                .deliveryTime(LocalDateTime.now())
                .build();
        orderMapper.updateById(updateDO);
        return Response.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response closeOrder(CloseOmsOrderReqVO reqVO) {
        OmsOrderDO orderDO = getValidOrder(reqVO.getId());
        // 允许关闭：待付款(0)、待发货(1)
        if (!Objects.equals(orderDO.getStatus(), 0) && !Objects.equals(orderDO.getStatus(), 1)) {
            throw new BizException(ResponseCodeEnum.ORDER_STATUS_ILLEGAL);
        }

        OmsOrderDO updateDO = OmsOrderDO.builder()
                .id(orderDO.getId())
                .status(4)
                .note(reqVO.getNote())
                .build();
        orderMapper.updateById(updateDO);

        // 如果是待付款订单，解锁库存
        if (Objects.equals(orderDO.getStatus(), 0)) {
            unlockStock(orderDO.getId());
            log.info("取消待付款订单，已解锁库存, orderId={}", orderDO.getId());
        }
        // 如果是待发货订单，恢复库存
        else if (Objects.equals(orderDO.getStatus(), 1)) {
            restoreStock(orderDO.getId());
            log.info("取消待发货订单，已恢复库存, orderId={}", orderDO.getId());
        }

        // 恢复优惠券
        rollbackCoupon(orderDO);

        return Response.success();
    }

    @Override
    public Response remarkOrder(RemarkOmsOrderReqVO reqVO) {
        OmsOrderDO orderDO = getValidOrder(reqVO.getId());
        // 已关闭(4)/无效(5)不允许修改备注（可按需放开）
        if (Objects.equals(orderDO.getStatus(), 4) || Objects.equals(orderDO.getStatus(), 5)) {
            throw new BizException(ResponseCodeEnum.ORDER_STATUS_ILLEGAL);
        }

        OmsOrderDO updateDO = OmsOrderDO.builder()
                .id(orderDO.getId())
                .note(reqVO.getNote())
                .build();
        orderMapper.updateById(updateDO);
        return Response.success();
    }

    private OmsOrderDO getValidOrder(Long id) {
        if (Objects.isNull(id) || id <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }
        OmsOrderDO orderDO = orderMapper.selectById(id);
        if (Objects.isNull(orderDO) || Objects.equals(orderDO.getIsDeleted(), 1)) {
            throw new BizException(ResponseCodeEnum.ORDER_NOT_FOUND);
        }
        return orderDO;
    }

    /**
     * 解锁库存（待付款订单取消时）
     */
    private void unlockStock(Long orderId) {
        try {
            LambdaQueryWrapper<OmsOrderItemDO> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(OmsOrderItemDO::getOrderId, orderId);
            List<OmsOrderItemDO> items = orderItemMapper.selectList(wrapper);

            for (OmsOrderItemDO item : items) {
                if (Objects.nonNull(item.getProductSkuId()) && item.getProductSkuId() > 0) {
                    int unlockResult = pmsSkuStockMapper.unlockStock(item.getProductSkuId(), item.getProductQuantity());
                    if (unlockResult > 0) {
                        log.info("库存已解锁, skuId={}, quantity={}, orderId={}", item.getProductSkuId(), item.getProductQuantity(), orderId);
                    } else {
                        log.warn("库存解锁失败, skuId={}, quantity={}, orderId=", item.getProductSkuId(), item.getProductQuantity(), orderId);
                    }
                }
            }
        } catch (Exception e) {
            log.error("解锁库存异常, orderId={}", orderId, e);
        }
    }

    /**
     * 恢复库存（待发货订单取消时）
     */
    private void restoreStock(Long orderId) {
        try {
            LambdaQueryWrapper<OmsOrderItemDO> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(OmsOrderItemDO::getOrderId, orderId);
            List<OmsOrderItemDO> items = orderItemMapper.selectList(wrapper);

            for (OmsOrderItemDO item : items) {
                if (Objects.nonNull(item.getProductSkuId()) && item.getProductSkuId() > 0) {
                    int restoreResult = pmsSkuStockMapper.restoreStock(item.getProductSkuId(), item.getProductQuantity());
                    if (restoreResult > 0) {
                        log.info("库存已恢复, skuId={}, quantity={}, orderId=", item.getProductSkuId(), item.getProductQuantity(), orderId);
                    } else {
                        log.warn("库存恢复失败, skuId={}, quantity={}, orderId={}", item.getProductSkuId(), item.getProductQuantity(), orderId);
                    }
                }
            }
        } catch (Exception e) {
            log.error("恢复库存异常, orderId={}", orderId, e);
        }
    }

    /**
     * 回滚优惠券
     */
    private void rollbackCoupon(OmsOrderDO order) {
        if (Objects.isNull(order.getCouponId()) || order.getCouponId() <= 0) {
            return;
        }

        try {
            LambdaQueryWrapper<SmsCouponHistoryDO> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(SmsCouponHistoryDO::getOrderId, order.getId())
                    .eq(SmsCouponHistoryDO::getCouponId, order.getCouponId())
                    .eq(SmsCouponHistoryDO::getUseStatus, 1)
                    .last("LIMIT 1");
            SmsCouponHistoryDO history = smsCouponHistoryMapper.selectOne(wrapper);

            if (Objects.nonNull(history)) {
                SmsCouponHistoryDO updateHistory = new SmsCouponHistoryDO();
                updateHistory.setId(history.getId());
                updateHistory.setUseStatus(0);
                updateHistory.setUseTime(null);
                updateHistory.setOrderId(null);
                updateHistory.setOrderSn(null);
                smsCouponHistoryMapper.updateById(updateHistory);

                smsCouponMapper.decrementUseCount(order.getCouponId());
                log.info("优惠券已回滚, couponId={}, orderId={}", order.getCouponId(), order.getId());
            }
        } catch (Exception e) {
            log.error("回滚优惠券异常, orderId={}, couponId={}", order.getId(), order.getCouponId(), e);
        }
    }
}

