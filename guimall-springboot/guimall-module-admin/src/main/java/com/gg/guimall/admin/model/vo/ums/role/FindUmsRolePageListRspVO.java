package com.gg.guimall.admin.model.vo.ums.role;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 角色分页查询响应 VO
 */
@Data
@ApiModel("角色分页查询响应 VO")
public class FindUmsRolePageListRspVO {
    private Long id;
    private String name;
    private String remark;
    private Integer userCount;
}

