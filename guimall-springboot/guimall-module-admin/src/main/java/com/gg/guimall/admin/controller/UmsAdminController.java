package com.gg.guimall.admin.controller;

import com.gg.guimall.admin.model.vo.ums.admin.*;
import com.gg.guimall.admin.service.UmsAdminService;
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
@RequestMapping("/admin/ums/admin")
@Api(tags = "后台账号管理")
public class UmsAdminController {

    @Autowired
    private UmsAdminService adminService;

    @Autowired
    private UmsRoleService roleService;

    @PostMapping("/list")
    @ApiOperation("分页查询后台账号")
    @ApiOperationLog(description = "分页查询后台账号")
    public PageResponse<FindUmsAdminPageListRspVO> list(@RequestBody @Validated FindUmsAdminPageListReqVO reqVO) {
        return adminService.findAdminPageList(reqVO);
    }

    @GetMapping("/roleOptions")
    @ApiOperation("获取角色下拉选项")
    public List<com.gg.guimall.admin.model.vo.ums.role.RoleOptionRspVO> roleOptions() {
        return roleService.findRoleOptions();
    }

    @PostMapping("/create")
    @ApiOperation("新增后台账号")
    @ApiOperationLog(description = "新增后台账号")
    public Response create(@RequestBody @Validated CreateUmsAdminReqVO reqVO) {
        return adminService.createAdmin(reqVO);
    }

    @PostMapping("/update")
    @ApiOperation("修改后台账号")
    @ApiOperationLog(description = "修改后台账号")
    public Response update(@RequestBody @Validated UpdateUmsAdminReqVO reqVO) {
        return adminService.updateAdmin(reqVO);
    }

    @PostMapping("/delete")
    @ApiOperation("删除后台账号（批量）")
    @ApiOperationLog(description = "删除后台账号（批量）")
    public Response delete(@RequestBody List<Long> ids) {
        return adminService.deleteAdmins(ids);
    }

    @PostMapping("/assignRole")
    @ApiOperation("分配后台账号角色")
    @ApiOperationLog(description = "分配后台账号角色")
    public Response assignRole(@RequestBody @Validated AssignUmsAdminRoleReqVO reqVO) {
        return adminService.assignRole(reqVO);
    }
}

