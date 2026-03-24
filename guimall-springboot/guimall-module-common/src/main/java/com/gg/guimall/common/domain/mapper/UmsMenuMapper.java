package com.gg.guimall.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gg.guimall.common.domain.dos.UmsMenuDO;

import java.util.List;

/**
 * 菜单 Mapper：t_menu
 */
public interface UmsMenuMapper extends BaseMapper<UmsMenuDO> {

    default List<UmsMenuDO> selectByParentId(Long parentId) {
        LambdaQueryWrapper<UmsMenuDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UmsMenuDO::getParentId, parentId);
        return selectList(wrapper);
    }

}

