package com.gg.guimall.common.domain.dos;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * 后台菜单表：t_menu
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_menu")
public class UmsMenuDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("parent_id")
    private Long parentId;

    private String title;

    /**
     * 前端权限树的“路由名称/Key”来源。
     */
    private String name;

    private String path;

    private String icon;

    private Integer sort;

    /**
     * 0 显示，1 隐藏
     */
    private Integer hidden;

    @TableField("create_time")
    private LocalDateTime createTime;
}

