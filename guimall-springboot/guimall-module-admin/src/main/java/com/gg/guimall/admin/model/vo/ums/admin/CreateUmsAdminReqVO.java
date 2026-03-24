package com.gg.guimall.admin.model.vo.ums.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 创建后台账号请求 VO
 */
@Data
@NoArgsConstructor
@ApiModel("创建后台账号请求 VO")
public class CreateUmsAdminReqVO {

    @ApiModelProperty("登录账号")
    @NotBlank(message = "登录账号不能为空")
    private String username;

    @ApiModelProperty("登录密码")
    @NotBlank(message = "登录密码不能为空")
    @Size(min = 6, message = "密码至少 6 位")
    private String password;

    @ApiModelProperty("昵称（展示用）")
    @NotBlank(message = "昵称不能为空")
    private String nickname;

    @ApiModelProperty("角色ID")
    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    /**
     * 1 正常，0 禁用
     */
    @NotNull(message = "状态不能为空")
    private Integer status;
}

