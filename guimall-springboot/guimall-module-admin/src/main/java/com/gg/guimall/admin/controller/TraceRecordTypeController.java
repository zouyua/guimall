package com.gg.guimall.admin.controller;

import com.gg.guimall.common.aspect.ApiOperationLog;
import com.gg.guimall.common.domain.dos.TraceRecordTypeDO;
import com.gg.guimall.common.domain.mapper.TraceRecordTypeMapper;
import com.gg.guimall.common.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/04/03
 * @description: 溯源记录类型 Controller
 **/
@RestController
@RequestMapping("/admin/trace/recordType")
@Api(tags = "溯源记录类型管理")
public class TraceRecordTypeController {

    @Autowired
    private TraceRecordTypeMapper traceRecordTypeMapper;

    @GetMapping("/listByCategoryId")
    @ApiOperation(value = "根据商品分类ID获取溯源记录类型")
    @ApiOperationLog(description = "根据商品分类ID获取溯源记录类型")
    public Response listByCategoryId(@RequestParam Long categoryId) {
        List<TraceRecordTypeDO> list = traceRecordTypeMapper.selectByCategoryId(categoryId);
        return Response.success(list);
    }

    @GetMapping("/listAll")
    @ApiOperation(value = "获取所有溯源记录类型")
    @ApiOperationLog(description = "获取所有溯源记录类型")
    public Response listAll() {
        List<TraceRecordTypeDO> list = traceRecordTypeMapper.selectAllTypes();
        return Response.success(list);
    }
}
