package com.gg.guimall.admin.controller;

import com.gg.guimall.admin.model.vo.pms.PmsSkuStockVO;
import com.gg.guimall.admin.service.PmsSkuStockService;
import com.gg.guimall.common.aspect.ApiOperationLog;
import com.gg.guimall.common.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/18
 * @description: SKU库存 Controller
 **/
@RestController
@RequestMapping("/admin/pms/product/sku")
@Api(tags = "SKU库存管理")
public class PmsSkuStockController {

    @Autowired
    private PmsSkuStockService skuStockService;

    @GetMapping("/{productId}")
    @ApiOperation(value = "根据商品ID查询SKU列表")
    @ApiOperationLog(description = "根据商品ID查询SKU列表")
    public Response findSkuListByProductId(@PathVariable Long productId) {
        return skuStockService.findSkuListByProductId(productId);
    }

    @PostMapping("/{productId}/save")
    @ApiOperation(value = "批量保存SKU（覆盖更新）")
    @ApiOperationLog(description = "批量保存SKU")
    public Response saveSkuList(@PathVariable Long productId,
                                @RequestBody @Validated List<PmsSkuStockVO> skuList) {
        return skuStockService.saveSkuList(productId, skuList);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除SKU")
    @ApiOperationLog(description = "删除SKU")
    public Response deleteSku(@PathVariable Long id) {
        return skuStockService.deleteSku(id);
    }
}
