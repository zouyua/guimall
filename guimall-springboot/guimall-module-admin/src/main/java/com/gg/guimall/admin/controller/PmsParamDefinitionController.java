package com.gg.guimall.admin.controller;

import com.gg.guimall.admin.model.vo.pms.ParamDefinitionReqVO;
import com.gg.guimall.admin.service.PmsParamDefinitionService;
import com.gg.guimall.common.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 参数定义管理（挂在分类下）
 */
@RestController
@RequestMapping("/admin/productCategory")
@Api(tags = "Admin - 参数定义管理")
public class PmsParamDefinitionController {

    @Autowired
    private PmsParamDefinitionService pmsParamDefinitionService;

    @GetMapping("/{categoryId}/params")
    @ApiOperation("查询分类下的参数定义列表")
    public Response listParams(@PathVariable Long categoryId) {
        return pmsParamDefinitionService.listByCategoryId(categoryId);
    }

    @PostMapping("/{categoryId}/params")
    @ApiOperation("新增参数定义")
    public Response createParam(@PathVariable Long categoryId,
                                @Validated @RequestBody ParamDefinitionReqVO reqVO) {
        reqVO.setCategoryId(categoryId);
        return pmsParamDefinitionService.create(reqVO);
    }

    @PutMapping("/params/{id}")
    @ApiOperation("修改参数定义")
    public Response updateParam(@PathVariable Long id,
                                @Validated @RequestBody ParamDefinitionReqVO reqVO) {
        reqVO.setId(id);
        return pmsParamDefinitionService.update(reqVO);
    }

    @DeleteMapping("/params/{id}")
    @ApiOperation("删除参数定义")
    public Response deleteParam(@PathVariable Long id) {
        return pmsParamDefinitionService.delete(id);
    }
}
