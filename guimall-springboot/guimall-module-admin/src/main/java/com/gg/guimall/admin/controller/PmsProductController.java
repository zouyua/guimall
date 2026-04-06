package com.gg.guimall.admin.controller;

import com.gg.guimall.admin.model.vo.pms.*;
import com.gg.guimall.admin.service.PmsProductService;
import com.gg.guimall.common.aspect.ApiOperationLog;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*@author:wgg
 * @url:www.gg.com
 * @date:2026/3/10
 * @description:商品管理 Controller
 * */
@RestController("adminPmsProductController")
@RequestMapping("/admin/pms/product")
@Api(tags = "商品管理模块")
public class PmsProductController {

    @Autowired
    private PmsProductService pmsProductService;

    @PostMapping("/create")
    @ApiOperation(value = "创建商品")
    @ApiOperationLog(description = "创建商品")
    public Response createProduct(@RequestBody @Validated PmsProductCreateReqVO pmsProductCreateReqVO) {
        return pmsProductService.createProduct(pmsProductCreateReqVO);
    }

    @PostMapping("/list")
    @ApiOperation(value = "分页查询商品")
    @ApiOperationLog(description = "分页查询商品")
    public PageResponse findProductPageList(@RequestBody @Validated FindPmsProductPageListReqVO findPmsProductPageListReqVO) {
        return pmsProductService.findProductPageList(findPmsProductPageListReqVO);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查询商品详情")
    @ApiOperationLog(description = "查询商品详情")
    public Response findProductDetail(@PathVariable Long id) {
        return pmsProductService.findProductDetail(id);
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改商品")
    @ApiOperationLog(description = "修改商品")
    public Response updateProduct(@RequestBody @Validated PmsProductUpdateReqVO pmsProductUpdateReqVO) {
        return pmsProductService.updateProduct(pmsProductUpdateReqVO);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除商品")
    @ApiOperationLog(description = "删除商品")
    public Response deleteProduct(@PathVariable Long id) {
        return pmsProductService.deleteProduct(id);
    }

    @PutMapping("/{id}/publish")
    @ApiOperation(value = "上架商品")
    @ApiOperationLog(description = "上架商品")
    public Response publishProduct(@PathVariable Long id) {
        return pmsProductService.publishProduct(id);
    }

    @PutMapping("/{id}/unpublish")
    @ApiOperation(value = "下架商品")
    @ApiOperationLog(description = "下架商品")
    public Response unpublishProduct(@PathVariable Long id) {
        return pmsProductService.unpublishProduct(id);
    }

    @PostMapping("/batch/delete")
    @ApiOperation(value = "批量删除商品")
    @ApiOperationLog(description = "批量删除商品")
    public Response batchDelete(@RequestBody List<Long> ids) {
        return pmsProductService.batchDelete(ids);
    }

    @PostMapping("/batch/publish")
    @ApiOperation(value = "批量上架")
    @ApiOperationLog(description = "批量上架商品")
    public Response batchPublish(@RequestBody List<Long> ids) {
        return pmsProductService.batchUpdatePublishStatus(ids, 1);
    }

    @PostMapping("/batch/unpublish")
    @ApiOperation(value = "批量下架")
    @ApiOperationLog(description = "批量下架商品")
    public Response batchUnpublish(@RequestBody List<Long> ids) {
        return pmsProductService.batchUpdatePublishStatus(ids, 0);
    }
}
