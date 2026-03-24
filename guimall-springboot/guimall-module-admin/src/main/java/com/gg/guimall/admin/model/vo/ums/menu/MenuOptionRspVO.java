package com.gg.guimall.admin.model.vo.ums.menu;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 菜单下拉选项 VO
 */
@Data
@ApiModel("菜单下拉选项 VO")
public class MenuOptionRspVO {
    private Long id;
    private String name;
}

