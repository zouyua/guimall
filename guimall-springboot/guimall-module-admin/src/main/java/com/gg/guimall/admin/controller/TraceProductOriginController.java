package com.gg.guimall.admin.controller;

import com.gg.guimall.admin.service.TraceProductOriginService;
import com.gg.guimall.common.aspect.ApiOperationLog;
import com.gg.guimall.common.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 农户-产地关联查询 Controller
 * 保留按产地/农户查询接口（用于删除拦截），
 * 产地的绑定/解绑已移至农户管理（FarmerController → pms_farmer_origin）
 *
 * @author wly
 * @date 2026/4/4
 */
@RestController
@RequestMapping("/admin/trace/productOrigin")
@Api(tags = "农户产地关联查询模块")
public class TraceProductOriginController {

    @Autowired
    private TraceProductOriginService traceProductOriginService;

    @GetMapping("/origin/{originId}")
    @ApiOperation(value = "根据产地ID查询关联农户和商品")
    @ApiOperationLog(description = "根据产地ID查询关联农户和商品")
    public Response findByOriginId(@PathVariable Long originId) {
        return traceProductOriginService.findByOriginId(originId);
    }

    @GetMapping("/farmer/{farmerId}")
    @ApiOperation(value = "根据农户ID查询名下商品")
    @ApiOperationLog(description = "根据农户ID查询名下商品")
    public Response findByFarmerId(@PathVariable Long farmerId) {
        return traceProductOriginService.findByFarmerId(farmerId);
    }
}
