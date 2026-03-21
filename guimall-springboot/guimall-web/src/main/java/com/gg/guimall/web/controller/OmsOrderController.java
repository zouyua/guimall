package com.gg.guimall.web.controller;

import com.gg.guimall.common.aspect.ApiOperationLog;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 前台订单 Controller
 */
@RestController("webOmsOrderController")
@RequestMapping("/order")
@Api(tags = "订单模块")
public class OmsOrderController {

    @Autowired
    private OmsOrderService omsOrderService;

    @PostMapping("/submit")
    @ApiOperation(value = "提交订单（前台）")
    @ApiOperationLog(description = "提交订单（前台）")
    public Response submit(@RequestBody @Validated SubmitOmsOrderReqVO reqVO) {
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
}

