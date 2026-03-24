package com.gg.guimall.admin.model.vo.ums.menu;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

/**
 * 菜单分页查询响应 VO
 */
@Data
@ApiModel("菜单分页查询响应 VO")
public class FindUmsMenuPageListRspVO {
    private Long id;

    private String parentName;

    private String name;

    private String path;

    private String icon;

    private Integer sort;

    /**
     * 0 显示，1 隐藏
     */
    private Integer hidden;

    private Long parentId;

    private Date createTime;
}

