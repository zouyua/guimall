package com.gg.guimall.admin.controller;

import com.gg.guimall.admin.model.vo.sms.CreateSmsHomeNewProductReqVO;
import com.gg.guimall.admin.model.vo.sms.FindSmsHomeNewProductPageListReqVO;
import com.gg.guimall.admin.model.vo.sms.FindSmsHomeNewProductPageListRspVO;
import com.gg.guimall.admin.model.vo.sms.UpdateSmsHomeNewProductReqVO;
import com.gg.guimall.admin.service.SmsHomeNewProductService;
import com.gg.guimall.common.aspect.ApiOperationLog;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 新品推荐管理 Controller
 */
@RestController
@RequestMapping("/admin/sms/newProduct")
@Api(tags = "新品推荐管理模块")
public class SmsHomeNewProductController {

    @Autowired
    private SmsHomeNewProductService homeNewProductService;

    @PostMapping("/create")
    @ApiOperation(value = "创建新品推荐")
    @ApiOperationLog(description = "创建新品推荐")
    public Response create(@RequestBody @Validated CreateSmsHomeNewProductReqVO reqVO) {
        return homeNewProductService.createNewProduct(reqVO);
    }

    @PostMapping("/delete/{id}")
    @ApiOperation(value = "删除新品推荐")
    @ApiOperationLog(description = "删除新品推荐")
    public Response delete(@PathVariable Long id) {
        return homeNewProductService.deleteNewProduct(id);
    }

    @GetMapping("/list")
    @ApiOperation(value = "新品推荐列表分页查询")
    @ApiOperationLog(description = "新品推荐列表分页查询")
    public PageResponse<FindSmsHomeNewProductPageListRspVO> list(FindSmsHomeNewProductPageListReqVO reqVO) {
        return homeNewProductService.findNewProductPageList(reqVO);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "新品推荐详情")
    @ApiOperationLog(description = "新品推荐详情")
    public Response detail(@PathVariable Long id) {
        return homeNewProductService.findNewProductDetail(id);
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改新品推荐")
    @ApiOperationLog(description = "修改新品推荐")
    public Response update(@PathVariable Long id,
                             @RequestBody @Validated UpdateSmsHomeNewProductReqVO reqVO) {
        reqVO.setId(id);
        return homeNewProductService.updateNewProduct(reqVO);
    }

    @GetMapping("/options")
    @ApiOperation(value = "获取新品推荐下拉列表（仅推荐）")
    @ApiOperationLog(description = "获取新品推荐下拉列表（仅推荐）")
    public Response options() {
        return homeNewProductService.findNewProductOptions();
    }
}

