package com.gg.guimall.common.domain.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色-菜单关系：t_role_menu_relation
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_role_menu_relation")
public class UmsRoleMenuRelationDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 角色（权限认证时的 role code）
     */
    private String role;

    /**
     * 菜单 id
     */
    @com.baomidou.mybatisplus.annotation.TableField("menu_id")
    private Long menuId;
}

