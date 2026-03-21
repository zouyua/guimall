package com.gg.guimall.web.controller;

import com.gg.guimall.common.aspect.ApiOperationLog;
import com.gg.guimall.common.utils.Response;
import com.gg.guimall.web.service.TraceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 * @description: 溯源查询 Controller（前台）
 **/
@RestController
@RequestMapping("/trace")
@Api(tags = "溯源查询")
public class TraceController {

    @Autowired
    private TraceService traceService;

    @GetMapping("/{productId}")
    @ApiOperation("根据商品ID查询溯源详情（并累加扫码次数）")
    @ApiOperationLog(description = "根据商品ID查询溯源详情（并累加扫码次数）")
    public Response detail(@PathVariable Long productId) {
        return traceService.findTraceDetail(productId);
    }
}

