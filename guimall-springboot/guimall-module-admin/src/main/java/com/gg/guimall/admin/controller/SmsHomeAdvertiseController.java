package com.gg.guimall.admin.controller;

import com.gg.guimall.admin.model.vo.sms.CreateSmsHomeAdvertiseReqVO;
import com.gg.guimall.admin.model.vo.sms.FindSmsHomeAdvertisePageListReqVO;
import com.gg.guimall.admin.model.vo.sms.FindSmsHomeAdvertisePageListRspVO;
import com.gg.guimall.admin.model.vo.sms.UpdateSmsHomeAdvertiseReqVO;
import com.gg.guimall.admin.service.SmsHomeAdvertiseService;
import com.gg.guimall.common.aspect.ApiOperationLog;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 首页轮播广告管理 Controller
 */
@RestController
@RequestMapping("/admin/sms/advertise")
@Api(tags = "首页轮播广告管理模块")
public class SmsHomeAdvertiseController {

    @Autowired
    private SmsHomeAdvertiseService homeAdvertiseService;

    @PostMapping("/create")
    @ApiOperation(value = "创建广告")
    @ApiOperationLog(description = "创建广告")
    public Response create(@RequestBody @Validated CreateSmsHomeAdvertiseReqVO reqVO) {
        return homeAdvertiseService.createAdvertise(reqVO);
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改广告")
    @ApiOperationLog(description = "修改广告")
    public Response update(@PathVariable Long id,
                             @RequestBody @Validated UpdateSmsHomeAdvertiseReqVO reqVO) {
        reqVO.setId(id);
        return homeAdvertiseService.updateAdvertise(reqVO);
    }

    @PostMapping("/delete/{id}")
    @ApiOperation(value = "删除广告")
    @ApiOperationLog(description = "删除广告")
    public Response delete(@PathVariable Long id) {
        return homeAdvertiseService.deleteAdvertise(id);
    }

    @GetMapping("/list")
    @ApiOperation(value = "广告列表分页查询")
    @ApiOperationLog(description = "广告列表分页查询")
    public PageResponse<FindSmsHomeAdvertisePageListRspVO> list(FindSmsHomeAdvertisePageListReqVO reqVO) {
        return homeAdvertiseService.findAdvertisePageList(reqVO);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "广告详情")
    @ApiOperationLog(description = "广告详情")
    public Response detail(@PathVariable Long id) {
        return homeAdvertiseService.findAdvertiseDetail(id);
    }
}

