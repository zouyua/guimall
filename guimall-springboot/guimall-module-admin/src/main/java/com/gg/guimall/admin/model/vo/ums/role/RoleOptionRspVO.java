package com.gg.guimall.admin.model.vo.ums.role;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 角色下拉选项响应 VO
 */
@Data
@ApiModel("角色下拉选项响应 VO")
public class RoleOptionRspVO {
    private Long id;
    private String name;
}

