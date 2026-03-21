package com.gg.guimall.web.controller;

import com.gg.guimall.common.aspect.ApiOperationLog;
import com.gg.guimall.common.utils.Response;
import com.gg.guimall.web.service.HomeAdvertiseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页轮播广告 Controller（前台）
 */
@RestController
@RequestMapping("/home")
@Api(tags = "首页模块")
public class HomeAdvertiseController {

    @Autowired
    private HomeAdvertiseService homeAdvertiseService;

    @GetMapping("/advertises")
    @ApiOperation(value = "获取轮播广告列表")
    @ApiOperationLog(description = "获取轮播广告列表")
    public Response listAdvertises(@RequestParam Integer type) {
        return homeAdvertiseService.listAdvertises(type);
    }
}

