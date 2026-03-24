package com.gg.guimall.admin.model.vo.ums.menu;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 修改菜单请求 VO
 */
@Data
@NoArgsConstructor
@ApiModel("修改菜单请求 VO")
public class UpdateUmsMenuReqVO {

    @NotNull(message = "菜单ID不能为空")
    private Long id;

    @NotNull(message = "上级菜单ID不能为空")
    @Min(0)
    private Long parentId;

    @NotBlank(message = "菜单名称不能为空")
    private String name;

    private String path;

    private String icon;

    @NotNull(message = "排序不能为空")
    @Min(0)
    private Integer sort;

    @NotNull(message = "显示状态不能为空")
    private Integer hidden;
}

