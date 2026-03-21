package com.gg.guimall.admin.controller;

import com.gg.guimall.admin.model.vo.sms.CreateSmsHomeRecommendProductReqVO;
import com.gg.guimall.admin.model.vo.sms.FindSmsHomeRecommendProductPageListReqVO;
import com.gg.guimall.admin.model.vo.sms.FindSmsHomeRecommendProductPageListRspVO;
import com.gg.guimall.admin.model.vo.sms.UpdateSmsHomeRecommendProductReqVO;
import com.gg.guimall.admin.service.SmsHomeRecommendProductService;
import com.gg.guimall.common.aspect.ApiOperationLog;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 人气推荐管理 Controller
 */
@RestController
@RequestMapping("/admin/sms/recommendProduct")
@Api(tags = "人气推荐管理模块")
public class SmsHomeRecommendProductController {

    @Autowired
    private SmsHomeRecommendProductService homeRecommendProductService;

    @PostMapping("/create")
    @ApiOperation(value = "创建人气推荐")
    @ApiOperationLog(description = "创建人气推荐")
    public Response create(@RequestBody @Validated CreateSmsHomeRecommendProductReqVO reqVO) {
        return homeRecommendProductService.createRecommendProduct(reqVO);
    }

    @PostMapping("/delete/{id}")
    @ApiOperation(value = "删除人气推荐")
    @ApiOperationLog(description = "删除人气推荐")
    public Response delete(@PathVariable Long id) {
        return homeRecommendProductService.deleteRecommendProduct(id);
    }

    @GetMapping("/list")
    @ApiOperation(value = "人气推荐列表分页查询")
    @ApiOperationLog(description = "人气推荐列表分页查询")
    public PageResponse<FindSmsHomeRecommendProductPageListRspVO> list(FindSmsHomeRecommendProductPageListReqVO reqVO) {
        return homeRecommendProductService.findRecommendProductPageList(reqVO);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "人气推荐详情")
    @ApiOperationLog(description = "人气推荐详情")
    public Response detail(@PathVariable Long id) {
        return homeRecommendProductService.findRecommendProductDetail(id);
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改人气推荐")
    @ApiOperationLog(description = "修改人气推荐")
    public Response update(@PathVariable Long id,
                             @RequestBody @Validated UpdateSmsHomeRecommendProductReqVO reqVO) {
        reqVO.setId(id);
        return homeRecommendProductService.updateRecommendProduct(reqVO);
    }

    @GetMapping("/options")
    @ApiOperation(value = "获取人气推荐下拉列表（仅推荐）")
    @ApiOperationLog(description = "获取人气推荐下拉列表（仅推荐）")
    public Response options() {
        return homeRecommendProductService.findRecommendProductOptions();
    }
}

