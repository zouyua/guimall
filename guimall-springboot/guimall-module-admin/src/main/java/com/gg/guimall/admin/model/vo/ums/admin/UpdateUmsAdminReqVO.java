package com.gg.guimall.admin.model.vo.ums.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 修改后台账号请求 VO
 */
@Data
@NoArgsConstructor
@ApiModel("修改后台账号请求 VO")
public class UpdateUmsAdminReqVO {

    @NotNull(message = "后台账号ID不能为空")
    private Long id;

    @NotBlank(message = "登录账号不能为空")
    private String username;

    /**
     * 新密码（可留空不修改）
     */
    @Size(min = 6, message = "密码至少 6 位")
    private String password;

    @NotBlank(message = "昵称不能为空")
    private String nickname;

    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    @NotNull(message = "状态不能为空")
    private Integer status;
}

