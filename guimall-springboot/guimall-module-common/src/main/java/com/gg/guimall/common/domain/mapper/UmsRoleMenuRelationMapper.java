package com.gg.guimall.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gg.guimall.common.domain.dos.UmsRoleMenuRelationDO;

import java.util.List;

/**
 * 角色-菜单关系 Mapper：t_role_menu_relation
 */
public interface UmsRoleMenuRelationMapper extends BaseMapper<UmsRoleMenuRelationDO> {

    default List<UmsRoleMenuRelationDO> selectByRole(String role) {
        LambdaQueryWrapper<UmsRoleMenuRelationDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UmsRoleMenuRelationDO::getRole, role);
        return selectList(wrapper);
    }

    default int deleteByRole(String role) {
        LambdaQueryWrapper<UmsRoleMenuRelationDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UmsRoleMenuRelationDO::getRole, role);
        return delete(wrapper);
    }
}

