package com.gg.guimall.admin.model.vo.ums.role;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 保存分配菜单权限请求 VO
 */
@Data
@NoArgsConstructor
@ApiModel("保存分配菜单权限请求 VO")
public class SaveUmsRoleAllocMenuReqVO {

    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    /**
     * 已选菜单 key 列表（本项目：key = menuId 字符串）
     */
    private List<String> checkedKeys;
}

