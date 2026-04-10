package com.gg.guimall.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gg.guimall.common.domain.dos.OmsOrderDO;
import com.gg.guimall.common.domain.dos.OmsOrderItemDO;
import com.gg.guimall.common.domain.dos.OmsOrderReturnApplyDO;
import com.gg.guimall.common.domain.dos.OmsOrderReturnReasonDO;
import com.gg.guimall.common.domain.dos.SmsCouponHistoryDO;
import com.gg.guimall.common.domain.dos.UmsIntegrationHistoryDO;
import com.gg.guimall.common.domain.mapper.OmsOrderItemMapper;
import com.gg.guimall.common.domain.mapper.OmsOrderMapper;
import com.gg.guimall.common.domain.mapper.OmsOrderReturnApplyMapper;
import com.gg.guimall.common.domain.mapper.OmsOrderReturnReasonMapper;
import com.gg.guimall.common.domain.mapper.SmsCouponHistoryMapper;
import com.gg.guimall.common.domain.mapper.SmsCouponMapper;
import com.gg.guimall.common.domain.mapper.UmsIntegrationHistoryMapper;
import com.gg.guimall.common.domain.mapper.UmsMemberMapper;
import com.gg.guimall.common.enums.ResponseCodeEnum;
import com.gg.guimall.common.exception.BizException;
import com.gg.guimall.common.utils.Response;
import com.gg.guimall.web.model.vo.oms.CancelOmsOrderReqVO;
import com.gg.guimall.web.model.vo.oms.CreateOmsOrderReturnApplyReqVO;
import com.gg.guimall.web.model.vo.oms.FindOmsOrderReturnReasonListRspVO;
import com.gg.guimall.web.service.OmsOrderReturnApplyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 前台订单退货 Service 实现类
 */
@Service("webOmsOrderReturnApplyServiceImpl")
@Slf4j
public class OmsOrderReturnApplyServiceImpl implements OmsOrderReturnApplyService {

    @Autowired
    private OmsOrderReturnApplyMapper returnApplyMapper;

    @Autowired
    private OmsOrderReturnReasonMapper returnReasonMapper;

    @Autowired
    private OmsOrderMapper orderMapper;

    @Autowired
    private OmsOrderItemMapper orderItemMapper;

    @Autowired
    private SmsCouponHistoryMapper couponHistoryMapper;

    @Autowired
    private SmsCouponMapper couponMapper;

    @Autowired
    private UmsMemberMapper umsMemberMapper;

    @Autowired
    private UmsIntegrationHistoryMapper integrationHistoryMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response createReturnApply(CreateOmsOrderReturnApplyReqVO reqVO) {
        if (Objects.isNull(reqVO) || Objects.isNull(reqVO.getOrderId()) || reqVO.getOrderId() <= 0
                || Objects.isNull(reqVO.getMemberId()) || reqVO.getMemberId() <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        // 查询订单
        OmsOrderDO orderDO = orderMapper.selectById(reqVO.getOrderId());
        if (Objects.isNull(orderDO) || !Objects.equals(orderDO.getMemberId(), reqVO.getMemberId())) {
            throw new BizException(ResponseCodeEnum.ORDER_NOT_FOUND);
        }

        // 只有已支付的订单才能申请退货（状态 1,2,3）
        if (orderDO.getStatus() < 1 || orderDO.getStatus() > 3) {
            throw new BizException(ResponseCodeEnum.ORDER_STATUS_ILLEGAL);
        }

        // 查询订单商品（如果指定了 productId）
        String productPic = null;
        String productName = null;
        String productAttr = null;
        Integer productCount = null;
        java.math.BigDecimal productPrice = null;

        if (Objects.nonNull(reqVO.getProductId())) {
            List<OmsOrderItemDO> items = orderItemMapper.selectByOrderId(reqVO.getOrderId());
            OmsOrderItemDO targetItem = items.stream()
                    .filter(item -> Objects.equals(item.getProductId(), reqVO.getProductId()))
                    .findFirst()
                    .orElse(null);
            if (Objects.nonNull(targetItem)) {
                productPic = targetItem.getProductPic();
                productName = targetItem.getProductName();
                productAttr = targetItem.getProductAttr();
                productCount = targetItem.getProductQuantity();
                productPrice = targetItem.getProductPrice();
            }
        }

        // 创建退货申请
        OmsOrderReturnApplyDO applyDO = OmsOrderReturnApplyDO.builder()
                .orderId(reqVO.getOrderId())
                .orderSn(orderDO.getOrderSn())
                .productId(reqVO.getProductId())
                .memberUsername(orderDO.getMemberUsername())
                .returnAmount(reqVO.getReturnAmount())
                .returnName(reqVO.getReturnName())
                .returnPhone(reqVO.getReturnPhone())
                .status(0) // 待处理
                .productPic(productPic)
                .productName(productName)
                .productAttr(productAttr)
                .productCount(productCount)
                .productPrice(productPrice)
                .reason(reqVO.getReason())
                .description(reqVO.getDescription())
                .proofPics(reqVO.getProofPics())
                .deliverySn(reqVO.getDeliverySn())
                .createTime(LocalDateTime.now())
                .build();

        returnApplyMapper.insert(applyDO);

        return Response.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response cancelOrder(CancelOmsOrderReqVO reqVO) {
        if (Objects.isNull(reqVO) || Objects.isNull(reqVO.getOrderId()) || reqVO.getOrderId() <= 0
                || Objects.isNull(reqVO.getMemberId()) || reqVO.getMemberId() <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        // 查询订单
        OmsOrderDO orderDO = orderMapper.selectById(reqVO.getOrderId());
        if (Objects.isNull(orderDO) || !Objects.equals(orderDO.getMemberId(), reqVO.getMemberId())) {
            throw new BizException(ResponseCodeEnum.ORDER_NOT_FOUND);
        }

        // 只有待付款的订单才能取消（状态 0）
        if (!Objects.equals(orderDO.getStatus(), 0)) {
            throw new BizException(ResponseCodeEnum.ORDER_STATUS_ILLEGAL);
        }

        // 更新订单状态为已关闭（4）
        orderDO.setStatus(4);
        orderDO.setUpdateTime(LocalDateTime.now());
        orderMapper.updateById(orderDO);

        // 如果使用了优惠券，恢复优惠券
        if (Objects.nonNull(orderDO.getCouponId()) && orderDO.getCouponId() > 0) {
            LambdaQueryWrapper<SmsCouponHistoryDO> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(SmsCouponHistoryDO::getOrderId, orderDO.getId())
                    .eq(SmsCouponHistoryDO::getMemberId, reqVO.getMemberId())
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
            }
        }

        // 如果使用了积分，退还积分
        if (Objects.nonNull(orderDO.getUseIntegration()) && orderDO.getUseIntegration() > 0) {
            umsMemberMapper.addIntegration(reqVO.getMemberId(), orderDO.getUseIntegration());
            // 记录积分退还历史
            UmsIntegrationHistoryDO historyDO = UmsIntegrationHistoryDO.builder()
                    .memberId(reqVO.getMemberId())
                    .changeCount(orderDO.getUseIntegration())
                    .changeType(0) // 获取
                    .sourceType(4) // 取消退还
                    .sourceId(orderDO.getId())
                    .note("取消订单退还积分，订单号：" + orderDO.getOrderSn())
                    .createTime(LocalDateTime.now())
                    .build();
            integrationHistoryMapper.insert(historyDO);
        }

        return Response.success();
    }

    @Override
    public Response findReturnReasonList() {
        LambdaQueryWrapper<OmsOrderReturnReasonDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(OmsOrderReturnReasonDO::getStatus, 1)
                .orderByAsc(OmsOrderReturnReasonDO::getSort);

        List<OmsOrderReturnReasonDO> list = returnReasonMapper.selectList(wrapper);
        List<FindOmsOrderReturnReasonListRspVO> voList = list.stream()
                .map(reason -> FindOmsOrderReturnReasonListRspVO.builder()
                        .id(reason.getId())
                        .name(reason.getName())
                        .sort(reason.getSort())
                        .build())
                .collect(Collectors.toList());

        return Response.success(voList);
    }
}
