package com.gg.guimall.admin.model.vo.ums.admin;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

/**
 * 后台账号分页查询响应 VO
 */
@Data
@ApiModel("后台账号分页查询响应 VO")
public class FindUmsAdminPageListRspVO {

    private Long id;
    private String username;
    private String nickname;

    private Long roleId;
    private String roleName;

    /**
     * 1 正常，0 禁用
     */
    private Integer status;

    private Date createTime;
}

