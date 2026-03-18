package com.gg.guimall.admin.controller;

import com.gg.guimall.admin.model.vo.pms.*;
import com.gg.guimall.admin.service.PmsProductAttributeCategoryService;
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
 * @description: 商品属性分类 Controller
 **/
@RestController
@RequestMapping("/admin/pms/productAttr")
@Api(tags = "商品属性分类管理")
public class PmsProductAttributeCategoryController {

    @Autowired
    private PmsProductAttributeCategoryService attributeCategoryService;

    @PostMapping("/create")
    @ApiOperation(value = "创建属性分类")
    @ApiOperationLog(description = "创建属性分类")
    public Response createAttributeCategory(@RequestBody @Validated PmsProductAttributeCategoryCreateReqVO reqVO) {
        return attributeCategoryService.createAttributeCategory(reqVO);
    }

    @PostMapping("/list")
    @ApiOperation(value = "分页查询属性分类")
    @ApiOperationLog(description = "分页查询属性分类")
    public PageResponse findAttributeCategoryPageList(@RequestBody @Validated FindPmsProductAttributeCategoryPageReqVO reqVO) {
        return attributeCategoryService.findAttributeCategoryPageList(reqVO);
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改属性分类")
    @ApiOperationLog(description = "修改属性分类")
    public Response updateAttributeCategory(@RequestBody @Validated PmsProductAttributeCategoryUpdateReqVO reqVO) {
        return attributeCategoryService.updateAttributeCategory(reqVO);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除属性分类")
    @ApiOperationLog(description = "删除属性分类")
    public Response deleteAttributeCategory(@PathVariable Long id) {
        return attributeCategoryService.deleteAttributeCategory(id);
    }

    @GetMapping("/options")
    @ApiOperation(value = "获取属性分类下拉列表")
    @ApiOperationLog(description = "获取属性分类下拉列表")
    public Response findAttributeCategoryOptions() {
        return attributeCategoryService.findAttributeCategoryOptions();
    }
}
