package com.gg.guimall.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.common.domain.dos.OmsCartItemDO;
import com.gg.guimall.common.domain.dos.OmsOrderDO;
import com.gg.guimall.common.domain.dos.OmsOrderItemDO;
import com.gg.guimall.common.domain.dos.PmsProductDO;
import com.gg.guimall.common.domain.dos.SmsCouponDO;
import com.gg.guimall.common.domain.dos.SmsCouponHistoryDO;
import com.gg.guimall.common.domain.mapper.OmsCartItemMapper;
import com.gg.guimall.common.domain.mapper.OmsOrderItemMapper;
import com.gg.guimall.common.domain.mapper.OmsOrderMapper;
import com.gg.guimall.common.domain.mapper.PmsProductMapper;
import com.gg.guimall.common.domain.mapper.SmsCouponHistoryMapper;
import com.gg.guimall.common.domain.mapper.SmsCouponMapper;
import com.gg.guimall.common.enums.ResponseCodeEnum;
import com.gg.guimall.common.exception.BizException;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;
import com.gg.guimall.web.model.vo.oms.FindOmsOrderDetailReqVO;
import com.gg.guimall.web.model.vo.oms.FindOmsOrderDetailRspVO;
import com.gg.guimall.web.model.vo.oms.FindOmsOrderPageListReqVO;
import com.gg.guimall.web.model.vo.oms.FindOmsOrderPageListRspVO;
import com.gg.guimall.web.model.vo.oms.FindOmsOrderDetailRspVO.OrderItem;
import com.gg.guimall.web.model.vo.oms.SubmitOmsOrderReqVO;
import com.gg.guimall.web.model.vo.oms.SubmitOmsOrderRspVO;
import com.gg.guimall.web.constants.MQConstants;
import com.gg.guimall.web.model.dto.OrderTimeoutMessageDTO;
import com.gg.guimall.web.service.OmsOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 前台订单 Service 实现类
 */
@Service("webOmsOrderServiceImpl")
@Slf4j
public class OmsOrderServiceImpl implements OmsOrderService {

    @Autowired
    private OmsOrderMapper orderMapper;

    @Autowired
    private OmsOrderItemMapper orderItemMapper;

    @Autowired
    private OmsCartItemMapper cartItemMapper;

    @Autowired
    private PmsProductMapper pmsProductMapper;

    @Autowired
    private SmsCouponMapper smsCouponMapper;

    @Autowired
    private SmsCouponHistoryMapper smsCouponHistoryMapper;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response submitOrder(SubmitOmsOrderReqVO reqVO) {
        if (Objects.isNull(reqVO) || Objects.isNull(reqVO.getMemberId()) || reqVO.getMemberId() <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        List<OmsCartItemDO> cartItems = cartItemMapper.selectByMemberId(reqVO.getMemberId());
        if (Objects.isNull(cartItems) || cartItems.isEmpty()) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        LocalDateTime now = LocalDateTime.now();

        BigDecimal totalAmount = BigDecimal.ZERO;
        Long farmerId = null;

        for (OmsCartItemDO item : cartItems) {
            if (Objects.isNull(item) || Objects.isNull(item.getQuantity()) || item.getQuantity() <= 0) {
                continue;
            }
            BigDecimal price = Objects.isNull(item.getPrice()) ? BigDecimal.ZERO : item.getPrice();
            BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
            totalAmount = totalAmount.add(price.multiply(quantity));

            if (Objects.isNull(farmerId) && Objects.nonNull(item.getProductId())) {
                PmsProductDO productDO = pmsProductMapper.selectById(item.getProductId());
                if (Objects.nonNull(productDO)) {
                    farmerId = productDO.getFarmerId();
                }
            }
        }

        if (totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        // ========== 优惠券逻辑 ==========
        BigDecimal couponAmount = BigDecimal.ZERO;
        Long couponId = reqVO.getCouponId();
        SmsCouponHistoryDO matchedHistory = null;

        if (Objects.nonNull(couponId) && couponId > 0) {
            SmsCouponDO coupon = smsCouponMapper.selectById(couponId);
            if (Objects.nonNull(coupon)
                    && totalAmount.compareTo(coupon.getMinAmount()) >= 0
                    && now.isAfter(coupon.getStartTime())
                    && now.isBefore(coupon.getEndTime())) {
                // 查询该会员对此优惠券的未使用领取记录
                LambdaQueryWrapper<SmsCouponHistoryDO> historyWrapper = Wrappers.lambdaQuery();
                historyWrapper.eq(SmsCouponHistoryDO::getCouponId, couponId)
                        .eq(SmsCouponHistoryDO::getMemberId, reqVO.getMemberId())
                        .eq(SmsCouponHistoryDO::getUseStatus, 0)
                        .last("LIMIT 1");
                matchedHistory = smsCouponHistoryMapper.selectOne(historyWrapper);

                if (Objects.nonNull(matchedHistory)) {
                    couponAmount = coupon.getAmount();
                    // 原子递增使用数量
                    smsCouponMapper.incrementUseCount(couponId);
                } else {
                    // 会员未领取或已使用该优惠券，忽略couponId
                    couponId = null;
                }
            } else {
                // 优惠券不存在、不满足门槛或不在有效期内，忽略couponId
                couponId = null;
            }
        }

        BigDecimal payAmount = totalAmount.subtract(couponAmount);
        if (payAmount.compareTo(BigDecimal.ZERO) < 0) {
            payAmount = BigDecimal.ZERO;
        }

        String orderSn = "SN" + System.currentTimeMillis() + "_" + reqVO.getMemberId();

        OmsOrderDO orderDO = OmsOrderDO.builder()
                .memberId(reqVO.getMemberId())
                .farmerId(farmerId)
                .couponId(couponId)
                .orderSn(orderSn)
                .createTime(now)
                .updateTime(now)
                .memberUsername(null)
                .totalAmount(totalAmount)
                .payAmount(payAmount)
                .freightAmount(BigDecimal.ZERO)
                .promotionAmount(BigDecimal.ZERO)
                .couponAmount(couponAmount)
                .payType(0)
                .sourceType(0)
                .status(0)
                .receiverName(reqVO.getReceiverName())
                .receiverPhone(reqVO.getReceiverPhone())
                .receiverPostCode(reqVO.getReceiverPostCode())
                .receiverProvince(reqVO.getReceiverProvince())
                .receiverCity(reqVO.getReceiverCity())
                .receiverRegion(reqVO.getReceiverRegion())
                .receiverDetailAddress(reqVO.getReceiverDetailAddress())
                .note(reqVO.getNote())
                .confirmStatus(0)
                .isDeleted(0)
                .build();

        orderMapper.insert(orderDO);
        Long orderId = orderDO.getId();
        if (Objects.isNull(orderId) || orderId <= 0) {
            throw new BizException(ResponseCodeEnum.SYSTEM_ERROR);
        }

        // 标记优惠券领取记录为已使用
        if (Objects.nonNull(matchedHistory)) {
            matchedHistory.setUseStatus(1);
            matchedHistory.setUseTime(now);
            matchedHistory.setOrderId(orderId);
            matchedHistory.setOrderSn(orderSn);
            smsCouponHistoryMapper.updateById(matchedHistory);
        }

        // 发送 RocketMQ 延迟消息，30 分钟后自动取消未支付订单
        OrderTimeoutMessageDTO msgDTO = new OrderTimeoutMessageDTO(orderId, orderSn);
        rocketMQTemplate.syncSend(
                MQConstants.ORDER_TIMEOUT_TOPIC + ":" + MQConstants.ORDER_TIMEOUT_TAG,
                MessageBuilder.withPayload(msgDTO).build(),
                3000,
                MQConstants.ORDER_TIMEOUT_DELAY_LEVEL
        );
        log.info("订单超时延迟消息已发送, orderSn={}, delayLevel={}", orderSn, MQConstants.ORDER_TIMEOUT_DELAY_LEVEL);

        BigDecimal realAmount;
        for (OmsCartItemDO item : cartItems) {
            if (Objects.isNull(item) || Objects.isNull(item.getQuantity()) || item.getQuantity() <= 0) {
                continue;
            }

            BigDecimal price = Objects.isNull(item.getPrice()) ? BigDecimal.ZERO : item.getPrice();
            realAmount = price.multiply(BigDecimal.valueOf(item.getQuantity()));

            OmsOrderItemDO orderItemDO = OmsOrderItemDO.builder()
                    .orderId(orderId)
                    .orderSn(orderSn)
                    .productId(item.getProductId())
                    .productPic(item.getProductPic())
                    .productName(item.getProductName())
                    .productPrice(price)
                    .productQuantity(item.getQuantity())
                    .productSkuId(item.getProductSkuId())
                    .productSkuCode(item.getProductSkuCode())
                    .realAmount(realAmount)
                    .productAttr(item.getProductAttr())
                    .build();

            orderItemMapper.insert(orderItemDO);
        }

        // 提交成功：清空购物车
        cartItemMapper.clearByMemberId(reqVO.getMemberId());

        SubmitOmsOrderRspVO rspVO = new SubmitOmsOrderRspVO();
        rspVO.setOrderId(orderId);
        rspVO.setOrderSn(orderSn);
        rspVO.setTotalAmount(totalAmount);
        rspVO.setPayAmount(payAmount);
        rspVO.setCreateTime(now);
        return Response.success(rspVO);
    }

    @Override
    public PageResponse<FindOmsOrderPageListRspVO> findOrderPageList(FindOmsOrderPageListReqVO reqVO) {
        if (Objects.isNull(reqVO) || Objects.isNull(reqVO.getMemberId()) || reqVO.getMemberId() <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        Page<OmsOrderDO> page = orderMapper.selectPageList(
                reqVO.getCurrent(),
                reqVO.getSize(),
                null,
                reqVO.getOrderSn(),
                reqVO.getMemberId(),
                null,
                null,
                reqVO.getStatus(),
                null,
                null
        );

        List<FindOmsOrderPageListRspVO> voList = page.getRecords().stream()
                .map(order -> {
                    FindOmsOrderPageListRspVO vo = new FindOmsOrderPageListRspVO();
                    BeanUtils.copyProperties(order, vo);
                    return vo;
                })
                .collect(Collectors.toList());

        return PageResponse.success(page, voList);
    }

    @Override
    public Response findOrderDetail(FindOmsOrderDetailReqVO reqVO) {
        if (Objects.isNull(reqVO) || Objects.isNull(reqVO.getId()) || reqVO.getId() <= 0
                || Objects.isNull(reqVO.getMemberId()) || reqVO.getMemberId() <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        OmsOrderDO orderDO = orderMapper.selectById(reqVO.getId());
        if (Objects.isNull(orderDO) || Objects.equals(orderDO.getIsDeleted(), 1)
                || !Objects.equals(orderDO.getMemberId(), reqVO.getMemberId())) {
            throw new BizException(ResponseCodeEnum.ORDER_NOT_FOUND);
        }

        List<com.gg.guimall.common.domain.dos.OmsOrderItemDO> itemDOList = orderItemMapper.selectByOrderId(reqVO.getId());
        List<OrderItem> items = itemDOList.stream()
                .map(itemDO -> OrderItem.builder()
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
}

