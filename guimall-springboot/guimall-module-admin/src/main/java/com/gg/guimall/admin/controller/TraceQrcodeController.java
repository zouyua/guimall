package com.gg.guimall.admin.controller;

import com.gg.guimall.admin.model.vo.trace.UpsertTraceQrcodeReqVO;
import com.gg.guimall.admin.service.TraceQrcodeService;
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
 * @description: 溯源二维码 Controller
 **/
@RestController
@RequestMapping("/admin/trace/qrcode")
@Api(tags = "溯源二维码管理模块")
public class TraceQrcodeController {

    @Autowired
    private TraceQrcodeService traceQrcodeService;

    @PostMapping("/upsert")
    @ApiOperation(value = "新增/更新溯源二维码")
    @ApiOperationLog(description = "新增/更新溯源二维码")
    public Response upsert(@RequestBody @Validated UpsertTraceQrcodeReqVO reqVO) {
        return traceQrcodeService.upsert(reqVO);
    }

    @PostMapping("/generate/{productId}")
    @ApiOperation(value = "一键生成溯源二维码（ZXing生成 → MinIO上传 → 入库）")
    @ApiOperationLog(description = "一键生成溯源二维码")
    public Response generate(@PathVariable Long productId) {
        return traceQrcodeService.generate(productId);
    }

    @GetMapping("/product/{productId}")
    @ApiOperation(value = "根据商品ID查询溯源二维码")
    @ApiOperationLog(description = "根据商品ID查询溯源二维码")
    public Response findByProductId(@PathVariable Long productId) {
        return traceQrcodeService.findByProductId(productId);
    }

    @DeleteMapping("/product/{productId}")
    @ApiOperation(value = "删除商品的溯源二维码")
    @ApiOperationLog(description = "删除商品的溯源二维码")
    public Response deleteByProductId(@PathVariable Long productId) {
        return traceQrcodeService.deleteByProductId(productId);
    }
}
