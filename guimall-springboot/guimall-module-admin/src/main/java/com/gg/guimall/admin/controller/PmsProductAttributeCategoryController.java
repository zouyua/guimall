package com.gg.guimall.admin.controller;

import com.gg.guimall.admin.service.PmsProductAttributeCategoryService;
import com.gg.guimall.common.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品属性分类 Controller
 */
@RestController
@RequestMapping("/admin/pms/productAttr")
@RequiredArgsConstructor
@Api(tags = "商品属性分类管理")
public class PmsProductAttributeCategoryController {

    private final PmsProductAttributeCategoryService attributeCategoryService;

    @GetMapping("/options")
    @ApiOperation("获取商品属性分类下拉列表")
    public Response findAttributeCategoryOptions() {
        return Response.success(attributeCategoryService.findAttributeCategoryOptions());
    }
}
