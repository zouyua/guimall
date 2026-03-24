package com.gg.guimall.admin.model.vo.ums.admin;

import com.gg.guimall.common.model.BasePageQuery;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 后台账号分页查询请求 VO
 */
@Data
@NoArgsConstructor
@ApiModel("后台账号分页查询请求 VO")
public class FindUmsAdminPageListReqVO extends BasePageQuery {
    private String username;
    /**
     * 1 正常，0 禁用
     */
    private Integer status;
}

