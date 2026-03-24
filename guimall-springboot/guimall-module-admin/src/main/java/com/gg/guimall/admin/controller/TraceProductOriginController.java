package com.gg.guimall.admin.controller;

import com.gg.guimall.admin.model.vo.trace.BindProductOriginReqVO;
import com.gg.guimall.admin.service.TraceProductOriginService;
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
 * @description: 商品产地关联 Controller
 **/
@RestController
@RequestMapping("/admin/trace/productOrigin")
@Api(tags = "商品产地关联模块")
public class TraceProductOriginController {

    @Autowired
    private TraceProductOriginService traceProductOriginService;

    @PostMapping("/bind")
    @ApiOperation(value = "绑定商品产地")
    @ApiOperationLog(description = "绑定商品产地")
    public Response bind(@RequestBody @Validated BindProductOriginReqVO reqVO) {
        return traceProductOriginService.bind(reqVO);
    }

    @GetMapping("/product/{productId}")
    @ApiOperation(value = "根据商品ID查询产地关联")
    @ApiOperationLog(description = "根据商品ID查询产地关联")
    public Response findByProductId(@PathVariable Long productId) {
        return traceProductOriginService.findByProductId(productId);
    }

    @GetMapping("/origin/{originId}")
    @ApiOperation(value = "根据产地ID查询绑定商品")
    @ApiOperationLog(description = "根据产地ID查询绑定商品")
    public Response findByOriginId(@PathVariable Long originId) {
        return traceProductOriginService.findByOriginId(originId);
    }

    @GetMapping("/farmer/{farmerId}")
    @ApiOperation(value = "根据农户ID查询绑定商品")
    @ApiOperationLog(description = "根据农户ID查询绑定商品")
    public Response findByFarmerId(@PathVariable Long farmerId) {
        return traceProductOriginService.findByFarmerId(farmerId);
    }

    @DeleteMapping("/product/{productId}")
    @ApiOperation(value = "解除商品产地绑定")
    @ApiOperationLog(description = "解除商品产地绑定")
    public Response unbind(@PathVariable Long productId) {
        return traceProductOriginService.unbind(productId);
    }
}

