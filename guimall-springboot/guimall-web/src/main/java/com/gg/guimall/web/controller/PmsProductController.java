package com.gg.guimall.web.controller;

import com.gg.guimall.common.aspect.ApiOperationLog;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;
import com.gg.guimall.web.model.vo.pms.FindPmsProductPageListReqVO;
import com.gg.guimall.web.model.vo.pms.FindPmsProductDetailRspVO;
import com.gg.guimall.web.model.vo.pms.ProductCategoryTreeVO;
import com.gg.guimall.web.model.vo.pms.ProductPageItemVO;
import com.gg.guimall.web.service.PmsProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品前台 Controller
 */
@RestController("webPmsProductController")
@RequestMapping("/pms/product")
@Api(tags = "商品模块")
public class PmsProductController {

    @Autowired
    private PmsProductService pmsProductService;

    @GetMapping("/category/tree")
    @ApiOperation(value = "获取商品分类树（前台）")
    @ApiOperationLog(description = "获取商品分类树（前台）")
    public List<ProductCategoryTreeVO> categoryTree() {
        return pmsProductService.findCategoryTree();
    }

    @PostMapping("/list")
    @ApiOperation(value = "商品列表分页查询（前台）")
    @ApiOperationLog(description = "商品列表分页查询（前台）")
    public PageResponse<ProductPageItemVO> list(@RequestBody @Validated FindPmsProductPageListReqVO reqVO) {
        return pmsProductService.findProductPageList(reqVO);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "商品详情（前台）")
    @ApiOperationLog(description = "商品详情（前台）")
    public Response findDetail(@PathVariable Long id) {
        return pmsProductService.findProductDetail(id);
    }
}

