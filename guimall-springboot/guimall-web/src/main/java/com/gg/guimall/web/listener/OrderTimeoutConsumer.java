package com.gg.guimall.web.listener;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gg.guimall.common.domain.dos.OmsOrderDO;
import com.gg.guimall.common.domain.dos.SmsCouponHistoryDO;
import com.gg.guimall.common.domain.mapper.OmsOrderMapper;
import com.gg.guimall.common.domain.mapper.SmsCouponHistoryMapper;
import com.gg.guimall.common.domain.mapper.SmsCouponMapper;
import com.gg.guimall.web.constants.MQConstants;
import com.gg.guimall.web.model.dto.OrderTimeoutMessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 订单超时自动取消 - RocketMQ 延迟消息消费者
 */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = MQConstants.ORDER_TIMEOUT_TOPIC,
        selectorExpression = MQConstants.ORDER_TIMEOUT_TAG,
        consumerGroup = "guimall_order_timeout_group"
)
public class OrderTimeoutConsumer implements RocketMQListener<OrderTimeoutMessageDTO> {

    @Autowired
    private OmsOrderMapper omsOrderMapper;

    @Autowired
    private SmsCouponMapper smsCouponMapper;

    @Autowired
    private SmsCouponHistoryMapper smsCouponHistoryMapper;

    @Override
    public void onMessage(OrderTimeoutMessageDTO message) {
        String orderSn = message.getOrderSn();
        Long orderId = message.getOrderId();
        log.info("收到订单超时消息, orderId={}, orderSn={}", orderId, orderSn);

        try {
            OmsOrderDO order = omsOrderMapper.selectByOrderSn(orderSn);
            if (Objects.isNull(order)) {
                log.warn("订单不存在, orderSn={}", orderSn);
                return;
            }

            // 只取消未支付的订单（status=0）
            if (!Objects.equals(order.getStatus(), 0)) {
                log.info("订单状态非待付款, 无需取消, orderSn={}, status={}", orderSn, order.getStatus());
                return;
            }

            // 关闭订单
            OmsOrderDO updateOrder = new OmsOrderDO();
            updateOrder.setId(order.getId());
            updateOrder.setStatus(4);
            updateOrder.setAdminNote("超时未支付，系统自动取消");
            updateOrder.setUpdateTime(LocalDateTime.now());
            omsOrderMapper.updateById(updateOrder);
            log.info("订单已自动取消, orderSn={}, orderId={}", orderSn, order.getId());

            // 回滚优惠券
            rollbackCoupon(order);
        } catch (Exception e) {
            log.error("处理订单超时消息异常, orderSn={}", orderSn, e);
        }
    }

    /**
     * 回滚优惠券：重置领取记录状态 + 递减使用数量
     */
    private void rollbackCoupon(OmsOrderDO order) {
        if (Objects.isNull(order.getCouponId()) || order.getCouponId() <= 0) {
            return;
        }

        try {
            // 查询该订单关联的优惠券领取记录
            LambdaQueryWrapper<SmsCouponHistoryDO> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(SmsCouponHistoryDO::getOrderId, order.getId())
                    .eq(SmsCouponHistoryDO::getCouponId, order.getCouponId())
                    .eq(SmsCouponHistoryDO::getUseStatus, 1)
                    .last("LIMIT 1");
            SmsCouponHistoryDO history = smsCouponHistoryMapper.selectOne(wrapper);

            if (Objects.nonNull(history)) {
                // 重置领取记录为未使用
                SmsCouponHistoryDO updateHistory = new SmsCouponHistoryDO();
                updateHistory.setId(history.getId());
                updateHistory.setUseStatus(0);
                updateHistory.setUseTime(null);
                updateHistory.setOrderId(null);
                updateHistory.setOrderSn(null);
                smsCouponHistoryMapper.updateById(updateHistory);

                // 递减优惠券使用数量
                smsCouponMapper.decrementUseCount(order.getCouponId());
                log.info("优惠券已回滚, couponId={}, orderId={}", order.getCouponId(), order.getId());
            }
        } catch (Exception e) {
            log.error("回滚优惠券异常, orderId={}, couponId={}", order.getId(), order.getCouponId(), e);
        }
    }
}
