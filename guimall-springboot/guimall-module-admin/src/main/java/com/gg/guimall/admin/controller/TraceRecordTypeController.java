package com.gg.guimall.admin.controller;

import com.gg.guimall.common.aspect.ApiOperationLog;
import com.gg.guimall.common.domain.dos.TraceRecordTypeDO;
import com.gg.guimall.common.domain.mapper.TraceRecordTypeMapper;
import com.gg.guimall.common.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    @PostMapping("/create")
    @ApiOperation(value = "新增溯源记录类型")
    @ApiOperationLog(description = "新增溯源记录类型")
    public Response create(@RequestBody TraceRecordTypeDO recordType) {
        if (recordType.getCategoryId() == null) {
            return Response.fail("商品分类ID不能为空");
        }
        if (recordType.getTypeName() == null || recordType.getTypeName().trim().isEmpty()) {
            return Response.fail("记录类型名称不能为空");
        }
        recordType.setTypeName(recordType.getTypeName().trim());
        if (recordType.getSort() == null) {
            recordType.setSort(0);
        }
        recordType.setCreateTime(LocalDateTime.now());
        recordType.setUpdateTime(LocalDateTime.now());
        traceRecordTypeMapper.insert(recordType);
        return Response.success();
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新溯源记录类型")
    @ApiOperationLog(description = "更新溯源记录类型")
    public Response update(@RequestBody TraceRecordTypeDO recordType) {
        if (recordType.getId() == null) {
            return Response.fail("ID不能为空");
        }
        if (recordType.getTypeName() != null) {
            recordType.setTypeName(recordType.getTypeName().trim());
        }
        recordType.setUpdateTime(LocalDateTime.now());
        traceRecordTypeMapper.updateById(recordType);
        return Response.success();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除溯源记录类型")
    @ApiOperationLog(description = "删除溯源记录类型")
    public Response delete(@RequestParam Long id) {
        if (id == null) {
            return Response.fail("ID不能为空");
        }
        traceRecordTypeMapper.deleteById(id);
        return Response.success();
    }
}
