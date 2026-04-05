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
 * 参数定义管理（全局数据字典）
 */
@RestController
@RequestMapping("/admin/pms/paramDefinition")
@Api(tags = "Admin - 参数定义管理")
public class PmsParamDefinitionController {

    @Autowired
    private PmsParamDefinitionService pmsParamDefinitionService;

    @GetMapping("/list")
    @ApiOperation("查询所有参数定义")
    public Response listAll() {
        return pmsParamDefinitionService.listAll();
    }

    @GetMapping("/page")
    @ApiOperation("分页查询参数定义")
    public Response page(@RequestParam(defaultValue = "1") Integer current,
                        @RequestParam(defaultValue = "10") Integer size,
                        @RequestParam(required = false) String paramName) {
        return pmsParamDefinitionService.page(current, size, paramName);
    }

    @PostMapping
    @ApiOperation("新增参数定义")
    public Response create(@Validated @RequestBody ParamDefinitionReqVO reqVO) {
        return pmsParamDefinitionService.create(reqVO);
    }

    @PutMapping("/{id}")
    @ApiOperation("修改参数定义")
    public Response update(@PathVariable Long id,
                          @Validated @RequestBody ParamDefinitionReqVO reqVO) {
        reqVO.setId(id);
        return pmsParamDefinitionService.update(reqVO);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除参数定义")
    public Response delete(@PathVariable Long id) {
        return pmsParamDefinitionService.delete(id);
    }
}
