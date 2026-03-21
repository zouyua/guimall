package com.gg.guimall.web.controller;

import com.gg.guimall.common.aspect.ApiOperationLog;
import com.gg.guimall.common.utils.Response;
import com.gg.guimall.web.model.vo.cart.*;
import com.gg.guimall.web.service.OmsCartItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 * @description: 购物车 Controller
 **/
@RestController
@RequestMapping("/cart")
@Api(tags = "购物车管理")
public class OmsCartItemController {

    @Autowired
    private OmsCartItemService cartItemService;

    @PostMapping("/add")
    @ApiOperation("添加商品到购物车")
    @ApiOperationLog(description = "添加商品到购物车")
    public Response add(@RequestBody @Validated AddCartItemReqVO reqVO) {
        return cartItemService.add(reqVO);
    }

    @PostMapping("/clear")
    @ApiOperation("清空当前会员的购物车")
    @ApiOperationLog(description = "清空当前会员的购物车")
    public Response clear(@RequestBody @Validated ClearCartReqVO reqVO) {
        return cartItemService.clear(reqVO);
    }

    @PostMapping("/delete")
    @ApiOperation("删除购物车中的指定商品")
    @ApiOperationLog(description = "删除购物车中的指定商品")
    public Response delete(@RequestBody @Validated DeleteCartItemReqVO reqVO) {
        return cartItemService.delete(reqVO);
    }

    @GetMapping("/getProduct/{productId}")
    @ApiOperation("获取购物车中指定商品的规格，用于选择规格")
    @ApiOperationLog(description = "获取购物车中指定商品的规格")
    public Response getProduct(@PathVariable Long productId) {
        return cartItemService.getProduct(productId);
    }

    @GetMapping("/list")
    @ApiOperation("获取当前会员的购物车列表")
    @ApiOperationLog(description = "获取当前会员的购物车列表")
    public Response list(@RequestParam Long memberId) {
        return cartItemService.list(memberId);
    }

    @PostMapping("/update/attr")
    @ApiOperation("修改购物车中商品的规格")
    @ApiOperationLog(description = "修改购物车中商品的规格")
    public Response updateAttr(@RequestBody @Validated UpdateCartAttrReqVO reqVO) {
        return cartItemService.updateAttr(reqVO);
    }

    @GetMapping("/update/quantity")
    @ApiOperation("修改购物车中指定商品的数量")
    @ApiOperationLog(description = "修改购物车中指定商品的数量")
    public Response updateQuantity(@RequestParam Long memberId,
                                   @RequestParam Long id,
                                   @RequestParam Integer quantity) {
        return cartItemService.updateQuantity(memberId, id, quantity);
    }
}

