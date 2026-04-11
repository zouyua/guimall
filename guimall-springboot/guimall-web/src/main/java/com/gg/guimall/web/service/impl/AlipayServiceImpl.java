package com.gg.guimall.web.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.gg.guimall.common.config.AlipayConfig;
import com.gg.guimall.common.domain.dos.OmsOrderDO;
import com.gg.guimall.common.domain.dos.UmsMemberDO;
import com.gg.guimall.common.domain.mapper.OmsOrderMapper;
import com.gg.guimall.common.domain.mapper.UmsMemberMapper;
import com.gg.guimall.common.utils.Response;
import com.gg.guimall.web.service.AlipayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 支付宝支付 Service 实现类
 */
@Service
@Slf4j
public class AlipayServiceImpl implements AlipayService {

    @Autowired
    private AlipayConfig alipayConfig;

    @Autowired
    private OmsOrderMapper omsOrderMapper;

    @Autowired
    private UmsMemberMapper umsMemberMapper;

    @Override
    public Response createPay(String orderSn) {
        // 1. 查询订单
        OmsOrderDO order = omsOrderMapper.selectByOrderSn(orderSn);
        if (Objects.isNull(order)) {
            return Response.fail("订单不存在");
        }
        if (!Objects.equals(order.getStatus(), 0)) {
            return Response.fail("订单状态异常，无法支付");
        }

        try {
            // 2. 创建 AlipayClient
            AlipayClient alipayClient = new DefaultAlipayClient(
                    alipayConfig.getGatewayUrl(),
                    alipayConfig.getAppId(),
                    alipayConfig.getPrivateKey(),
                    "json",
                    alipayConfig.getCharset(),
                    alipayConfig.getAlipayPublicKey(),
                    alipayConfig.getSignType()
            );

            // 3. 创建支付请求
            AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
            request.setNotifyUrl(alipayConfig.getNotifyUrl());
            request.setReturnUrl(alipayConfig.getReturnUrl());

            // 4. 设置业务参数
            String subject = "Guimall订单-" + orderSn;
            if (order.getOrderType() != null && order.getOrderType() == 1) {
                subject = "Guimall会员开通-" + orderSn;
            }
            String bizContent = "{" +
                    "\"out_trade_no\":\"" + orderSn + "\"," +
                    "\"total_amount\":\"" + order.getPayAmount() + "\"," +
                    "\"subject\":\"" + subject + "\"," +
                    "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"" +
                    "}";
            request.setBizContent(bizContent);

            // 5. 调用支付宝接口，获取支付表单 HTML
            String formHtml = alipayClient.pageExecute(request).getBody();
            return Response.success(formHtml);

        } catch (AlipayApiException e) {
            log.error("创建支付宝支付失败, orderSn={}", orderSn, e);
            return Response.fail("创建支付失败: " + e.getMessage());
        }
    }

    @Override
    public String handleNotify(HttpServletRequest request) {
        try {
            // 1. 获取支付宝回调请求中的所有参数
            Map<String, String> paramsMap = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                String[] values = requestParams.get(name);
                StringBuilder valueStr = new StringBuilder();
                for (int i = 0; i < values.length; i++) {
                    valueStr.append(i == values.length - 1 ? values[i] : values[i] + ",");
                }
                paramsMap.put(name, valueStr.toString());
            }

            // 2. 验签
            boolean signVerified = AlipaySignature.rsaCheckV1(
                    paramsMap,
                    alipayConfig.getAlipayPublicKey(),
                    alipayConfig.getCharset(),
                    alipayConfig.getSignType()
            );

            if (signVerified) {
                // 3. 验签成功，获取交易信息
                String outTradeNo = paramsMap.get("out_trade_no");
                String tradeStatus = paramsMap.get("trade_status");
                String tradeNo = paramsMap.get("trade_no");

                log.info("支付宝异步通知验签成功, outTradeNo={}, tradeStatus={}, tradeNo={}", outTradeNo, tradeStatus, tradeNo);

                // 4. 交易成功，更新订单状态
                if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
                    OmsOrderDO order = omsOrderMapper.selectByOrderSn(outTradeNo);
                    if (Objects.nonNull(order) && Objects.equals(order.getStatus(), 0)) {
                        OmsOrderDO updateOrder = new OmsOrderDO();
                        updateOrder.setId(order.getId());
                        updateOrder.setStatus(1);
                        updateOrder.setPayTime(LocalDateTime.now());
                        updateOrder.setPayType(1);
                        updateOrder.setPaymentSn(tradeNo);
                        updateOrder.setUpdateTime(LocalDateTime.now());
                        omsOrderMapper.updateById(updateOrder);
                        log.info("订单支付成功，已更新状态, orderSn={}", outTradeNo);

                        // 会员等级订单：支付成功后自动开通等级
                        handleMemberLevelActivation(order);
                    }
                }

                return "success";
            } else {
                log.warn("支付宝异步通知验签失败");
                return "failure";
            }
        } catch (AlipayApiException e) {
            log.error("处理支付宝异步通知异常", e);
            return "failure";
        }
    }

    @Override
    public Response queryAndUpdateOrder(String orderSn) {
        // 1. 查询本地订单
        OmsOrderDO order = omsOrderMapper.selectByOrderSn(orderSn);
        if (Objects.isNull(order)) {
            return Response.fail("订单不存在");
        }
        // 已支付的直接返回
        if (!Objects.equals(order.getStatus(), 0)) {
            return Response.success("已支付");
        }

        try {
            // 2. 主动查询支付宝交易状态
            AlipayClient alipayClient = new DefaultAlipayClient(
                    alipayConfig.getGatewayUrl(),
                    alipayConfig.getAppId(),
                    alipayConfig.getPrivateKey(),
                    "json",
                    alipayConfig.getCharset(),
                    alipayConfig.getAlipayPublicKey(),
                    alipayConfig.getSignType()
            );

            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
            request.setBizContent("{\"out_trade_no\":\"" + orderSn + "\"}");

            AlipayTradeQueryResponse response = alipayClient.execute(request);
            log.info("主动查询支付宝交易状态, orderSn={}, tradeStatus={}", orderSn,
                    response.isSuccess() ? response.getTradeStatus() : "查询失败");

            if (response.isSuccess()) {
                String tradeStatus = response.getTradeStatus();
                if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
                    // 3. 更新订单状态
                    OmsOrderDO updateOrder = new OmsOrderDO();
                    updateOrder.setId(order.getId());
                    updateOrder.setStatus(1);
                    updateOrder.setPayTime(LocalDateTime.now());
                    updateOrder.setPayType(1);
                    updateOrder.setPaymentSn(response.getTradeNo());
                    updateOrder.setUpdateTime(LocalDateTime.now());
                    omsOrderMapper.updateById(updateOrder);
                    log.info("主动查询确认支付成功，已更新订单状态, orderSn={}", orderSn);

                    // 会员等级订单：支付成功后自动开通等级
                    handleMemberLevelActivation(order);

                    return Response.success("支付成功");
                }
                return Response.success("未支付");
            } else {
                return Response.success("未支付");
            }
        } catch (AlipayApiException e) {
            log.error("查询支付宝交易状态失败, orderSn={}", orderSn, e);
            return Response.fail("查询失败");
        }
    }

    /**
     * 会员等级订单支付成功后，自动激活会员等级
     * 从订单的 note 字段解析等级ID（格式："开通会员等级：XXX（等级ID：123）"）
     */
    private void handleMemberLevelActivation(OmsOrderDO order) {
        if (order.getOrderType() == null || order.getOrderType() != 1) {
            return; // 不是会员等级订单，跳过
        }
        try {
            // 从 note 中解析等级ID
            String note = order.getNote();
            if (note == null || !note.contains("等级ID：")) {
                log.warn("会员等级订单note格式异常, orderSn={}, note={}", order.getOrderSn(), note);
                return;
            }
            String levelIdStr = note.substring(note.indexOf("等级ID：") + 5, note.indexOf("）"));
            Long levelId = Long.parseLong(levelIdStr.trim());

            // 更新会员等级
            UmsMemberDO member = umsMemberMapper.selectById(order.getMemberId());
            if (member != null) {
                member.setMemberLevelId(levelId);
                member.setUpdateTime(LocalDateTime.now());
                umsMemberMapper.updateById(member);
                log.info("会员等级订单支付成功，已激活等级, memberId={}, levelId={}, orderSn={}",
                        order.getMemberId(), levelId, order.getOrderSn());
            }
        } catch (Exception e) {
            log.error("激活会员等级失败, orderSn={}", order.getOrderSn(), e);
        }
    }
}
