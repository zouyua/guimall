package com.gg.guimall.admin.controller;

import com.gg.guimall.admin.model.vo.pms.PmsProductFullReductionVO;
import com.gg.guimall.admin.service.PmsProductFullReductionService;
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
 * @description: 商品满减 Controller
 **/
@RestController
@RequestMapping("/admin/pms/product/fullReduction")
@Api(tags = "商品满减管理")
public class PmsProductFullReductionController {

    @Autowired
    private PmsProductFullReductionService fullReductionService;

    @GetMapping("/{productId}")
    @ApiOperation(value = "根据商品ID查询满减列表")
    @ApiOperationLog(description = "根据商品ID查询满减列表")
    public Response findFullReductionListByProductId(@PathVariable Long productId) {
        return fullReductionService.findFullReductionListByProductId(productId);
    }

    @PostMapping("/{productId}/save")
    @ApiOperation(value = "批量保存满减规则（覆盖更新）")
    @ApiOperationLog(description = "批量保存满减规则")
    public Response saveFullReductionList(@PathVariable Long productId,
                                          @RequestBody @Validated List<PmsProductFullReductionVO> fullReductionList) {
        return fullReductionService.saveFullReductionList(productId, fullReductionList);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除满减规则")
    @ApiOperationLog(description = "删除满减规则")
    public Response deleteFullReduction(@PathVariable Long id) {
        return fullReductionService.deleteFullReduction(id);
    }
}
