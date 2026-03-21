package com.gg.guimall.admin.controller;

import com.gg.guimall.admin.model.vo.trace.CreateOriginReqVO;
import com.gg.guimall.admin.model.vo.trace.FindOriginPageListReqVO;
import com.gg.guimall.admin.model.vo.trace.UpdateOriginReqVO;
import com.gg.guimall.admin.service.TraceOriginService;
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
 * @description: 产地管理 Controller
 **/
@RestController
@RequestMapping("/admin/trace/origin")
@Api(tags = "产地管理模块")
public class TraceOriginController {

    @Autowired
    private TraceOriginService traceOriginService;

    @PostMapping("/create")
    @ApiOperation(value = "创建产地")
    @ApiOperationLog(description = "创建产地")
    public Response create(@RequestBody @Validated CreateOriginReqVO reqVO) {
        return traceOriginService.createOrigin(reqVO);
    }

    @PostMapping("/list")
    @ApiOperation(value = "分页查询产地列表")
    @ApiOperationLog(description = "分页查询产地列表")
    public PageResponse list(@RequestBody @Validated FindOriginPageListReqVO reqVO) {
        return traceOriginService.findOriginPageList(reqVO);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查询产地详情")
    @ApiOperationLog(description = "查询产地详情")
    public Response detail(@PathVariable Long id) {
        return traceOriginService.findOriginDetail(id);
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改产地信息")
    @ApiOperationLog(description = "修改产地信息")
    public Response update(@RequestBody @Validated UpdateOriginReqVO reqVO) {
        return traceOriginService.updateOrigin(reqVO);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除产地")
    @ApiOperationLog(description = "删除产地")
    public Response delete(@PathVariable Long id) {
        return traceOriginService.deleteOrigin(id);
    }

    @GetMapping("/options")
    @ApiOperation(value = "获取产地下拉列表")
    @ApiOperationLog(description = "获取产地下拉列表")
    public Response options() {
        return traceOriginService.findOriginOptions();
    }
}

