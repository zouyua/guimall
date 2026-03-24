package com.gg.guimall.admin.model.vo.ums.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 分配后台账号角色请求 VO
 */
@Data
@NoArgsConstructor
@ApiModel("分配后台账号角色请求 VO")
public class AssignUmsAdminRoleReqVO {
    @ApiModelProperty("后台账号ID")
    @NotNull(message = "后台账号ID不能为空")
    private Long id;

    @ApiModelProperty("角色ID")
    @NotNull(message = "角色ID不能为空")
    private Long roleId;
}

