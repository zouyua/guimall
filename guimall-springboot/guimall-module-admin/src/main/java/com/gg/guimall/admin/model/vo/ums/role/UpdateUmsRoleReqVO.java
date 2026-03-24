package com.gg.guimall.admin.model.vo.ums.role;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 修改角色请求 VO
 */
@Data
@NoArgsConstructor
@ApiModel("修改角色请求 VO")
public class UpdateUmsRoleReqVO {

    @NotNull(message = "角色ID不能为空")
    private Long id;

    /**
     * 新角色名称（role code）
     */
    @NotBlank(message = "角色名称不能为空")
    private String name;

    private String remark;
}

