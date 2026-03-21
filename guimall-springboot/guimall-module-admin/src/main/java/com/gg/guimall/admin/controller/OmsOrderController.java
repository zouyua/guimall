package com.gg.guimall.admin.controller;

import com.gg.guimall.admin.model.vo.oms.CloseOmsOrderReqVO;
import com.gg.guimall.admin.model.vo.oms.DeliverOmsOrderReqVO;
import com.gg.guimall.admin.model.vo.oms.FindOmsOrderPageListReqVO;
import com.gg.guimall.admin.model.vo.oms.FindOmsOrderPageListRspVO;
import com.gg.guimall.admin.model.vo.oms.RemarkOmsOrderReqVO;
import com.gg.guimall.admin.service.OmsOrderService;
import com.gg.guimall.common.aspect.ApiOperationLog;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 * @description: 订单管理 Controller
 **/
@RestController("adminOmsOrderController")
@RequestMapping("/admin/oms/order")
@Api(tags = "订单管理模块")
public class OmsOrderController {

    @Autowired
    private OmsOrderService orderService;

    @PostMapping("/list")
    @ApiOperation(value = "订单列表分页查询")
    @ApiOperationLog(description = "订单列表分页查询")
    public PageResponse<FindOmsOrderPageListRspVO> findOrderPageList(
            @RequestBody @Validated FindOmsOrderPageListReqVO reqVO) {
        return orderService.findOrderPageList(reqVO);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "订单详情")
    @ApiOperationLog(description = "订单详情")
    public Response findOrderDetail(@PathVariable Long id) {
        return orderService.findOrderDetail(id);
    }

    @PostMapping("/deliver")
    @ApiOperation(value = "订单发货")
    @ApiOperationLog(description = "订单发货")
    public Response deliverOrder(@RequestBody @Validated DeliverOmsOrderReqVO reqVO) {
        return orderService.deliverOrder(reqVO);
    }

    @PostMapping("/close")
    @ApiOperation(value = "关闭订单")
    @ApiOperationLog(description = "关闭订单")
    public Response closeOrder(@RequestBody @Validated CloseOmsOrderReqVO reqVO) {
        return orderService.closeOrder(reqVO);
    }

    @PostMapping("/remark")
    @ApiOperation(value = "备注订单")
    @ApiOperationLog(description = "备注订单")
    public Response remarkOrder(@RequestBody @Validated RemarkOmsOrderReqVO reqVO) {
        return orderService.remarkOrder(reqVO);
    }
}

