package com.gg.guimall.web.controller;

import com.gg.guimall.common.aspect.ApiOperationLog;
import com.gg.guimall.common.utils.Response;
import com.gg.guimall.web.service.HomeNewProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页新品推荐 Controller
 */
@RestController
@RequestMapping("/home")
@Api(tags = "首页模块")
public class HomeNewProductController {

    @Autowired
    private HomeNewProductService homeNewProductService;

    @GetMapping("/new-products")
    @ApiOperation(value = "获取新品推荐列表")
    @ApiOperationLog(description = "获取新品推荐列表")
    public Response listNewProducts() {
        return homeNewProductService.listNewProducts();
    }
}

