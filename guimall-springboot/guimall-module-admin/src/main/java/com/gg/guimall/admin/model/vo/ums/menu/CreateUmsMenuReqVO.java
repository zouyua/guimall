package com.gg.guimall.admin.model.vo.ums.menu;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 创建菜单请求 VO
 */
@Data
@NoArgsConstructor
@ApiModel("创建菜单请求 VO")
public class CreateUmsMenuReqVO {

    @ApiModelProperty("上级菜单ID；顶级菜单为 0")
    @NotNull(message = "上级菜单ID不能为空")
    @Min(value = 0, message = "上级菜单ID不能为负数")
    private Long parentId;

    @ApiModelProperty("菜单名称（侧栏显示）")
    @NotBlank(message = "菜单名称不能为空")
    private String name;

    @ApiModelProperty("路由路径（可选）")
    private String path;

    @ApiModelProperty("图标组件名（可选）")
    private String icon;

    @ApiModelProperty("排序")
    @NotNull(message = "排序不能为空")
    @Min(value = 0, message = "排序不能为负数")
    private Integer sort;

    /**
     * 0 显示，1 隐藏
     */
    @ApiModelProperty("显示状态")
    @NotNull(message = "显示状态不能为空")
    private Integer hidden;
}

