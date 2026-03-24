package com.gg.guimall.admin.controller;

import com.gg.guimall.admin.model.vo.ums.role.*;
import com.gg.guimall.admin.service.UmsRoleService;
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
@RequestMapping("/admin/ums/role")
@Api(tags = "角色管理")
public class UmsRoleController {

    @Autowired
    private UmsRoleService roleService;

    @PostMapping("/list")
    @ApiOperation("分页查询角色")
    @ApiOperationLog(description = "分页查询角色")
    public PageResponse<FindUmsRolePageListRspVO> list(@RequestBody @Validated FindUmsRolePageListReqVO reqVO) {
        return roleService.findRolePageList(reqVO);
    }

    @GetMapping("/options")
    @ApiOperation("角色下拉选项")
    public List<RoleOptionRspVO> options() {
        return roleService.findRoleOptions();
    }

    @PostMapping("/create")
    @ApiOperation("新增角色")
    @ApiOperationLog(description = "新增角色")
    public Response create(@RequestBody @Validated CreateUmsRoleReqVO reqVO) {
        return roleService.createRole(reqVO);
    }

    @PostMapping("/update")
    @ApiOperation("修改角色")
    @ApiOperationLog(description = "修改角色")
    public Response update(@RequestBody @Validated UpdateUmsRoleReqVO reqVO) {
        return roleService.updateRole(reqVO);
    }

    @PostMapping("/delete")
    @ApiOperation("删除角色（批量）")
    @ApiOperationLog(description = "删除角色（批量）")
    public Response delete(@RequestBody List<Long> ids) {
        return roleService.deleteRoles(ids);
    }

    @GetMapping("/allocMenu/init")
    @ApiOperation("初始化角色分配菜单页面")
    public Response<AllocMenuInitRspVO> initAllocMenu(@RequestParam Long roleId) {
        return Response.success(roleService.initAllocMenu(roleId));
    }

    @PostMapping("/allocMenu/save")
    @ApiOperation("保存角色分配菜单权限")
    @ApiOperationLog(description = "保存角色分配菜单权限")
    public Response saveAllocMenu(@RequestBody @Validated SaveUmsRoleAllocMenuReqVO reqVO) {
        return roleService.saveAllocMenu(reqVO);
    }
}

