package com.gg.guimall.admin.model.vo.ums.menu;

import com.gg.guimall.common.model.BasePageQuery;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 菜单分页查询请求 VO
 */
@Data
@NoArgsConstructor
@ApiModel("菜单分页查询请求 VO")
public class FindUmsMenuPageListReqVO extends BasePageQuery {

    /**
     * 菜单名称（模糊搜索）
     */
    private String name;

    /**
     * 仅用于兼容前端字段：页面里叫“hidden/显示”
     * 这里不强制；为空则不筛选
     */
    private Integer hidden;
}

