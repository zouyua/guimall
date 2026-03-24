package com.gg.guimall.admin.model.vo.ums.role;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 创建角色请求 VO
 */
@Data
@NoArgsConstructor
@ApiModel("创建角色请求 VO")
public class CreateUmsRoleReqVO {

    /**
     * 角色名称（在本项目里直接作为 role code 持久化）
     */
    @NotBlank(message = "角色名称不能为空")
    private String name;

    /**
     * 描述
     * 说明：该字段目前主要用于前端展示，持久化以 role code 为准
     */
    private String remark;
}

