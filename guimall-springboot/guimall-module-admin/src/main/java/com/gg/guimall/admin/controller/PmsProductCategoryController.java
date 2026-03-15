package com.gg.guimall.admin.controller;

import com.gg.guimall.admin.model.vo.pms.*;
import com.gg.guimall.admin.service.PmsProductCategoryService;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/12
 * @description: 商品分类 Controller
 */
@RestController
@RequestMapping("/admin/product/category")
@RequiredArgsConstructor
@Api(tags = "商品分类管理")
public class PmsProductCategoryController {

    /**
     * 注入 Service
     */
    private final PmsProductCategoryService categoryService;

    /**
     * 新增商品分类
     */
    @PostMapping
    @ApiOperation("新增商品分类")
    public Response addCategory(@RequestBody PmsProductCategoryCreateReqVO reqVO) {

        categoryService.addCategory(reqVO);
        return Response.success();
    }

    /**
     * 修改商品分类
     */
    @PutMapping
    @ApiOperation("修改商品分类")
    public Response updateCategory(@RequestBody @Validated PmsProductCategoryUpdateReqVO reqVO) {

        categoryService.updateCategory(reqVO);
        return Response.success();
    }

    /**
     * 删除商品分类
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除商品分类")
    public Response deleteCategory(@PathVariable Long id) {

        categoryService.deleteCategory(id);
        return Response.success();
    }

    /**
     * 商品分类分页查询（POST + 请求体，与商品/农户分页方式一致）
     */
    @PostMapping("/list")
    @ApiOperation("分页查询商品分类")
    public PageResponse findCategoryPage(
            @RequestBody @Validated FindPmsProductCategoryPageReqVO reqVO) {

        return categoryService.findCategoryPage(reqVO);
    }

    /**
     * 查询商品分类树
     */
    @GetMapping("/tree")
    @ApiOperation("查询商品分类树")
    public List<FindPmsProductCategoryTreeRspVO> findCategoryTree() {

        return categoryService.findCategoryTree();
    }
}