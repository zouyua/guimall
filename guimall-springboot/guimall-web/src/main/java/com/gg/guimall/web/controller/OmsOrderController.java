package com.gg.guimall.web.controller;

import com.gg.guimall.common.aspect.ApiOperationLog;
import com.gg.guimall.common.domain.dos.OmsOrderDO;
import com.gg.guimall.common.domain.mapper.OmsOrderMapper;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;
import com.gg.guimall.web.model.vo.oms.FindOmsOrderDetailReqVO;
import com.gg.guimall.web.model.vo.oms.FindOmsOrderDetailRspVO;
import com.gg.guimall.web.model.vo.oms.FindOmsOrderPageListReqVO;
import com.gg.guimall.web.model.vo.oms.FindOmsOrderPageListRspVO;
import com.gg.guimall.web.model.vo.oms.SubmitOmsOrderReqVO;
import com.gg.guimall.web.model.vo.oms.SubmitOmsOrderRspVO;
import com.gg.guimall.web.service.OmsOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 前台订单 Controller
 */
@RestController("webOmsOrderController")
@RequestMapping("/order")
@Api(tags = "订单模块")
@Slf4j
public class OmsOrderController {

    @Autowired
    private OmsOrderService omsOrderService;

    @Autowired
    private OmsOrderMapper omsOrderMapper;

    /** 订单支付超时时间（分钟） */
    private static final long ORDER_TIMEOUT_MINUTES = 30;

    @PostMapping("/submit")
    @ApiOperation(value = "提交订单（前台）")
    @ApiOperationLog(description = "提交订单（前台）")
    public Response submit(@RequestBody @Validated SubmitOmsOrderReqVO reqVO) {
        log.info("收到订单提交请求: memberId={}, couponId={}, totalAmount={}, payAmount={}",
                reqVO.getMemberId(), reqVO.getCouponId(), reqVO.getTotalAmount(), reqVO.getPayAmount());
        return omsOrderService.submitOrder(reqVO);
    }

    @PostMapping("/list")
    @ApiOperation(value = "订单列表分页查询（前台）")
    @ApiOperationLog(description = "订单列表分页查询（前台）")
    public PageResponse<FindOmsOrderPageListRspVO> list(@RequestBody @Validated FindOmsOrderPageListReqVO reqVO) {
        return omsOrderService.findOrderPageList(reqVO);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "订单详情（前台）")
    @ApiOperationLog(description = "订单详情（前台）")
    public Response detail(@PathVariable Long id, @RequestParam Long memberId) {
        FindOmsOrderDetailReqVO reqVO = new FindOmsOrderDetailReqVO();
        reqVO.setId(id);
        reqVO.setMemberId(memberId);
        return omsOrderService.findOrderDetail(reqVO);
    }

    @GetMapping("/remaining-time")
    @ApiOperation(value = "查询订单剩余支付时间（秒）")
    @ApiOperationLog(description = "查询订单剩余支付时间（秒）")
    public Response getRemainingTime(@RequestParam String orderSn) {
        OmsOrderDO order = omsOrderMapper.selectByOrderSn(orderSn);
        if (Objects.isNull(order) || Objects.isNull(order.getCreateTime())) {
            return Response.success(0);
        }
        // 非待付款状态，无需倒计时
        if (!Objects.equals(order.getStatus(), 0)) {
            return Response.success(0);
        }
        long elapsed = Duration.between(order.getCreateTime(), LocalDateTime.now()).getSeconds();
        long remaining = ORDER_TIMEOUT_MINUTES * 60 - elapsed;
        return Response.success(Math.max(remaining, 0));
    }

    @PostMapping("/confirmReceipt")
    @ApiOperation(value = "确认收货（前台）")
    @ApiOperationLog(description = "确认收货（前台）")
    public Response confirmReceipt(@RequestParam Long orderId, @RequestParam Long memberId) {
        return omsOrderService.confirmReceipt(orderId, memberId);
    }
}

