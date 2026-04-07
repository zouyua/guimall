package com.gg.guimall.web.controller;

import com.gg.guimall.common.aspect.ApiOperationLog;
import com.gg.guimall.common.utils.Response;
import com.gg.guimall.web.model.vo.oms.CancelOmsOrderReqVO;
import com.gg.guimall.web.model.vo.oms.CreateOmsOrderReturnApplyReqVO;
import com.gg.guimall.web.service.OmsOrderReturnApplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 前台订单退货 Controller
 */
@RestController
@RequestMapping("/order/return")
@Api(tags = "订单退货（前台）")
public class OmsOrderReturnApplyController {

    @Autowired
    private OmsOrderReturnApplyService returnApplyService;

    @PostMapping("/apply")
    @ApiOperation(value = "创建退货申请")
    @ApiOperationLog(description = "创建退货申请（前台）")
    public Response createReturnApply(@RequestBody @Validated CreateOmsOrderReturnApplyReqVO reqVO) {
        return returnApplyService.createReturnApply(reqVO);
    }

    @PostMapping("/cancel")
    @ApiOperation(value = "取消订单")
    @ApiOperationLog(description = "取消订单（前台）")
    public Response cancelOrder(@RequestBody @Validated CancelOmsOrderReqVO reqVO) {
        return returnApplyService.cancelOrder(reqVO);
    }

    @GetMapping("/reasons")
    @ApiOperation(value = "查询退货原因列表")
    @ApiOperationLog(description = "查询退货原因列表（前台）")
    public Response findReturnReasonList() {
        return returnApplyService.findReturnReasonList();
    }
}
