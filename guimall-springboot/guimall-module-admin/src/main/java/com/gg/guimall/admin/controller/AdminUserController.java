package com.gg.guimall.admin.controller;

import com.gg.guimall.admin.model.vo.user.UpdateAdminUserPasswordReqVO;
import com.gg.guimall.admin.service.AdminUserService;
import com.gg.guimall.common.aspect.ApiOperationLog;
import com.gg.guimall.common.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*@author:wgg
 * @url:www.gg.com
 * @date:2026/3/7
 * @description:添加更新用户密码的接口
 * */
@RestController
@RequestMapping("/admin")
@Api(tags = "Admin 用户模块")
public class AdminUserController {
    @Autowired
    private AdminUserService userService;

    @PostMapping("/password/update")
    @ApiOperation(value = "修改用户密码")
    @ApiOperationLog(description = "修改用户密码")
    public Response updatePassword(@RequestBody @Validated UpdateAdminUserPasswordReqVO updateAdminUserPasswordReqVO) {
        return userService.updatePassword(updateAdminUserPasswordReqVO);
    }

    /*
    * 添加查询当前用户信息接口*/
    @PostMapping("/user/info")
    @ApiOperation(value = "获取用户信息")
    @ApiOperationLog(description = "获取用户信息")
    public Response findUserInfo() {
        return  userService.findUserInfo();
    }
}

