package com.gg.guimall.admin.controller;

import com.gg.guimall.admin.model.vo.oms.FindOmsOrderReturnApplyPageReqVO;
import com.gg.guimall.admin.model.vo.oms.FindOmsOrderReturnApplyPageRspVO;
import com.gg.guimall.admin.model.vo.oms.UpdateOmsOrderReturnApplyStatusReqVO;
import com.gg.guimall.admin.service.OmsOrderReturnApplyService;
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
 * @description: 退货申请 Controller
 **/
@RestController
@RequestMapping("/admin/oms/orderReturnApply")
@Api(tags = "订单退货管理")
public class OmsOrderReturnApplyController {

    @Autowired
    private OmsOrderReturnApplyService returnApplyService;

    @PostMapping("/list")
    @ApiOperation(value = "分页查询退货申请")
    @ApiOperationLog(description = "分页查询退货申请")
    public PageResponse<FindOmsOrderReturnApplyPageRspVO> findReturnApplyPageList(
            @RequestBody @Validated FindOmsOrderReturnApplyPageReqVO reqVO) {
        return returnApplyService.findReturnApplyPageList(reqVO);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "获取退货申请详情")
    @ApiOperationLog(description = "获取退货申请详情")
    public Response findReturnApplyDetail(@PathVariable Long id) {
        return returnApplyService.findReturnApplyDetail(id);
    }

    @PostMapping("/status/update")
    @ApiOperation(value = "修改退货申请状态")
    @ApiOperationLog(description = "修改退货申请状态")
    public Response updateReturnApplyStatus(@RequestBody @Validated UpdateOmsOrderReturnApplyStatusReqVO reqVO) {
        return returnApplyService.updateReturnApplyStatus(reqVO);
    }
}

