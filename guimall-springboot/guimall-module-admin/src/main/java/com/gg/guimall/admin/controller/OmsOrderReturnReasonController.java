package com.gg.guimall.admin.controller;

import com.gg.guimall.admin.model.vo.oms.*;
import com.gg.guimall.admin.service.OmsOrderReturnReasonService;
import com.gg.guimall.common.aspect.ApiOperationLog;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 * @description: 退货原因 Controller
 **/
@RestController
@RequestMapping("/admin/oms/returnReason")
@Api(tags = "退货原因管理")
public class OmsOrderReturnReasonController {

    @Autowired
    private OmsOrderReturnReasonService returnReasonService;

    @GetMapping("/{id}")
    @ApiOperation("获取单个退货原因详情信息")
    @ApiOperationLog(description = "获取单个退货原因详情信息")
    public Response getReturnReason(@PathVariable Long id) {
        return returnReasonService.findReturnReasonDetail(id);
    }

    @PostMapping("/create")
    @ApiOperation("添加退货原因")
    @ApiOperationLog(description = "添加退货原因")
    public Response create(@RequestBody @Validated CreateOmsOrderReturnReasonReqVO reqVO) {
        return returnReasonService.createReturnReason(reqVO);
    }

    @PostMapping("/delete")
    @ApiOperation("批量删除退货原因")
    @ApiOperationLog(description = "批量删除退货原因")
    public Response delete(@RequestBody List<Long> ids) {
        return returnReasonService.deleteReturnReasons(ids);
    }

    @GetMapping("/list")
    @ApiOperation("分页查询退货原因")
    @ApiOperationLog(description = "分页查询退货原因")
    public PageResponse<FindOmsOrderReturnReasonPageRspVO> list(FindOmsOrderReturnReasonPageReqVO reqVO) {
        return returnReasonService.findReturnReasonPageList(reqVO);
    }

    @PostMapping("/list")
    @ApiOperation("分页查询退货原因（POST）")
    @ApiOperationLog(description = "分页查询退货原因（POST）")
    public PageResponse<FindOmsOrderReturnReasonPageRspVO> listByPost(
            @RequestBody @Validated FindOmsOrderReturnReasonPageReqVO reqVO) {
        return returnReasonService.findReturnReasonPageList(reqVO);
    }

    @PostMapping("/update/{id}")
    @ApiOperation("修改退货原因")
    @ApiOperationLog(description = "修改退货原因")
    public Response update(@PathVariable Long id,
                           @RequestBody @Validated UpdateOmsOrderReturnReasonReqVO reqVO) {
        reqVO.setId(id);
        return returnReasonService.updateReturnReason(reqVO);
    }

    @PostMapping("/update/status")
    @ApiOperation("修改退货原因启用状态")
    @ApiOperationLog(description = "修改退货原因启用状态")
    public Response updateStatus(@RequestBody @Validated UpdateOmsOrderReturnReasonStatusReqVO reqVO) {
        return returnReasonService.updateReturnReasonStatus(reqVO);
    }
}

