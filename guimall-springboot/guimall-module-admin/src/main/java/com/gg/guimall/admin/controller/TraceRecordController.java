package com.gg.guimall.admin.controller;

import com.gg.guimall.admin.model.vo.trace.CreateTraceRecordReqVO;
import com.gg.guimall.admin.model.vo.trace.FindTraceRecordListReqVO;
import com.gg.guimall.admin.model.vo.trace.UpdateTraceRecordReqVO;
import com.gg.guimall.admin.service.TraceRecordService;
import com.gg.guimall.common.aspect.ApiOperationLog;
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
 * @description: 溯源记录 Controller
 **/
@RestController
@RequestMapping("/admin/trace/record")
@Api(tags = "溯源记录管理模块")
public class TraceRecordController {

    @Autowired
    private TraceRecordService traceRecordService;

    @PostMapping("/create")
    @ApiOperation(value = "创建溯源记录")
    @ApiOperationLog(description = "创建溯源记录")
    public Response create(@RequestBody @Validated CreateTraceRecordReqVO reqVO) {
        return traceRecordService.create(reqVO);
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改溯源记录")
    @ApiOperationLog(description = "修改溯源记录")
    public Response update(@RequestBody @Validated UpdateTraceRecordReqVO reqVO) {
        return traceRecordService.update(reqVO);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除溯源记录")
    @ApiOperationLog(description = "删除溯源记录")
    public Response delete(@PathVariable Long id) {
        return traceRecordService.delete(id);
    }

    @PostMapping("/list")
    @ApiOperation(value = "查询溯源记录列表（按商品）")
    @ApiOperationLog(description = "查询溯源记录列表（按商品）")
    public Response list(@RequestBody @Validated FindTraceRecordListReqVO reqVO) {
        return traceRecordService.list(reqVO);
    }
}

