package com.gg.guimall.admin.controller;

import com.gg.guimall.admin.model.vo.ums.menu.*;
import com.gg.guimall.admin.service.UmsMenuService;
import com.gg.guimall.common.aspect.ApiOperationLog;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/ums/menu")
@Api(tags = "菜单管理")
public class UmsMenuController {

    @Autowired
    private UmsMenuService menuService;

    @PostMapping("/list")
    @ApiOperation("分页查询菜单")
    @ApiOperationLog(description = "分页查询菜单")
    public PageResponse<FindUmsMenuPageListRspVO> list(@RequestBody @Validated FindUmsMenuPageListReqVO reqVO) {
        return menuService.findMenuPageList(reqVO);
    }

    @GetMapping("/options")
    @ApiOperation("获取顶级菜单下拉选项")
    public List<MenuOptionRspVO> options() {
        return menuService.findMenuOptions();
    }

    @PostMapping("/create")
    @ApiOperation("新增菜单")
    @ApiOperationLog(description = "新增菜单")
    public Response create(@RequestBody @Validated CreateUmsMenuReqVO reqVO) {
        return menuService.createMenu(reqVO);
    }

    @PostMapping("/update")
    @ApiOperation("修改菜单")
    @ApiOperationLog(description = "修改菜单")
    public Response update(@RequestBody @Validated UpdateUmsMenuReqVO reqVO) {
        return menuService.updateMenu(reqVO);
    }

    @PostMapping("/delete")
    @ApiOperation("删除菜单（批量）")
    @ApiOperationLog(description = "删除菜单（批量）")
    public Response delete(@RequestBody List<Long> ids) {
        return menuService.deleteMenus(ids);
    }
}

