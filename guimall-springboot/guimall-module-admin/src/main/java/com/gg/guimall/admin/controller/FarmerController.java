package com.gg.guimall.admin.controller;

import com.gg.guimall.admin.model.vo.farmer.*;
import com.gg.guimall.admin.service.FarmerService;
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
 * @date: 2026/3/12
 * @description: 农户管理 Controller
 **/
@RestController
@RequestMapping("/admin/farmer")
@Api(tags = "农户管理模块")
public class FarmerController {

    @Autowired
    private FarmerService farmerService;

    @PostMapping("/create")
    @ApiOperation(value = "创建农户")
    @ApiOperationLog(description = "创建农户")
    public Response createFarmer(@RequestBody @Validated CreateFarmerReqVO createFarmerReqVO) {
        return farmerService.createFarmer(createFarmerReqVO);
    }

    @PostMapping("/list")
    @ApiOperation(value = "分页查询农户列表")
    @ApiOperationLog(description = "分页查询农户列表")
    public PageResponse findFarmerPageList(@RequestBody @Validated FindFarmerPageListReqVO findFarmerPageListReqVO) {
        return farmerService.findFarmerPageList(findFarmerPageListReqVO);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查询农户详情")
    @ApiOperationLog(description = "查询农户详情")
    public Response findFarmerDetail(@PathVariable Long id) {
        return farmerService.findFarmerDetail(id);
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改农户信息")
    @ApiOperationLog(description = "修改农户信息")
    public Response updateFarmer(@RequestBody @Validated UpdateFarmerReqVO updateFarmerReqVO) {
        return farmerService.updateFarmer(updateFarmerReqVO);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除农户")
    @ApiOperationLog(description = "删除农户")
    public Response deleteFarmer(@PathVariable Long id) {
        return farmerService.deleteFarmer(id);
    }

    @GetMapping("/options")
    @ApiOperation(value = "获取农户下拉列表")
    @ApiOperationLog(description = "获取农户下拉列表")
    public Response findFarmerOptions() {
        return farmerService.findFarmerOptions();
    }
}