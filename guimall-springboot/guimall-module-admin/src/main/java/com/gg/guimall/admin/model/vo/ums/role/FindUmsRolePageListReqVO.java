package com.gg.guimall.admin.model.vo.ums.role;

import com.gg.guimall.common.model.BasePageQuery;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色分页查询请求 VO
 */
@Data
@NoArgsConstructor
@ApiModel("角色分页查询请求 VO")
public class FindUmsRolePageListReqVO extends BasePageQuery {

    /**
     * 角色名称（模糊搜索）
     */
    private String name;
}

