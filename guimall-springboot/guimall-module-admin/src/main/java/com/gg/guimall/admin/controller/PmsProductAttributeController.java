package com.gg.guimall.admin.controller;

import com.gg.guimall.admin.model.vo.pms.*;
import com.gg.guimall.admin.service.PmsProductAttributeService;
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
 * @date: 2026/3/13
 * @description: 商品属性 Controller
 **/
@RestController
@RequestMapping("/admin/pms/productAttr/productAttrList")
@Api(tags = "商品属性管理")
public class PmsProductAttributeController {

    @Autowired
    private PmsProductAttributeService attributeService;

    @PostMapping("/create")
    @ApiOperation(value = "创建商品属性")
    @ApiOperationLog(description = "创建商品属性")
    public Response createAttribute(@RequestBody @Validated PmsProductAttributeCreateReqVO reqVO) {
        return attributeService.createAttribute(reqVO);
    }

    @PostMapping("/list")
    @ApiOperation(value = "分页查询商品属性")
    @ApiOperationLog(description = "分页查询商品属性")
    public PageResponse findAttributePageList(@RequestBody @Validated FindPmsProductAttributePageReqVO reqVO) {
        return attributeService.findAttributePageList(reqVO);
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改商品属性")
    @ApiOperationLog(description = "修改商品属性")
    public Response updateAttribute(@RequestBody @Validated PmsProductAttributeUpdateReqVO reqVO) {
        return attributeService.updateAttribute(reqVO);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除商品属性")
    @ApiOperationLog(description = "删除商品属性")
    public Response deleteAttribute(@PathVariable Long id) {
        return attributeService.deleteAttribute(id);
    }

    @GetMapping("/category/{categoryId}")
    @ApiOperation(value = "根据分类ID查询属性列表")
    @ApiOperationLog(description = "根据分类ID查询属性列表")
    public Response findAttributeListByCategoryId(
            @PathVariable Long categoryId,
            @RequestParam(required = false) Integer type) {
        return attributeService.findAttributeListByCategoryId(categoryId, type);
    }
}
