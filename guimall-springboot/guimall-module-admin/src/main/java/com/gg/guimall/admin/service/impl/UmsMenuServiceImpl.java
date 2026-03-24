package com.gg.guimall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.admin.model.vo.ums.menu.*;
import com.gg.guimall.admin.service.UmsMenuService;
import com.gg.guimall.common.domain.dos.UmsMenuDO;
import com.gg.guimall.common.domain.mapper.UmsMenuMapper;
import com.gg.guimall.common.domain.mapper.UmsRoleMenuRelationMapper;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UmsMenuServiceImpl implements UmsMenuService {

    @Autowired
    private UmsMenuMapper menuMapper;

    @Autowired
    private UmsRoleMenuRelationMapper roleMenuRelationMapper;

    @Override
    public PageResponse<FindUmsMenuPageListRspVO> findMenuPageList(FindUmsMenuPageListReqVO reqVO) {
        Page<UmsMenuDO> page = new Page<>(reqVO.getCurrent(), reqVO.getSize());
        LambdaQueryWrapper<UmsMenuDO> wrapper = new LambdaQueryWrapper<>();
        if (reqVO.getName() != null && !reqVO.getName().trim().isEmpty()) {
            wrapper.like(UmsMenuDO::getTitle, reqVO.getName().trim());
        }
        if (reqVO.getHidden() != null) {
            wrapper.eq(UmsMenuDO::getHidden, reqVO.getHidden());
        }

        menuMapper.selectPage(page, wrapper);

        List<UmsMenuDO> records = page.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return PageResponse.success(page, Collections.emptyList());
        }

        Set<Long> parentIds = records.stream()
                .map(UmsMenuDO::getParentId)
                .filter(pid -> pid != null && pid > 0)
                .collect(Collectors.toSet());

        Map<Long, String> parentIdToTitle = Collections.<Long, String>emptyMap();
        if (!parentIds.isEmpty()) {
            List<UmsMenuDO> parents = menuMapper.selectList(
                    new LambdaQueryWrapper<UmsMenuDO>().in(UmsMenuDO::getId, parentIds)
            );
            parentIdToTitle = parents.stream().collect(Collectors.toMap(UmsMenuDO::getId, UmsMenuDO::getTitle, (a, b) -> a));
        }

        List<FindUmsMenuPageListRspVO> rspVOS = records.stream().map(m -> {
            FindUmsMenuPageListRspVO vo = new FindUmsMenuPageListRspVO();
            vo.setId(m.getId());
            vo.setParentId(m.getParentId());
            vo.setName(m.getTitle());
            vo.setPath(m.getPath());
            vo.setIcon(m.getIcon());
            vo.setSort(m.getSort());
            vo.setHidden(m.getHidden());
            vo.setCreateTime(m.getCreateTime());
            return vo;
        }).collect(Collectors.toList());

        // 补齐 parentName（避免在 lambda 内做 Map 访问导致的编译兼容问题）
        for (FindUmsMenuPageListRspVO vo : rspVOS) {
            Long pid = vo.getParentId();
            if (pid == null || pid <= 0) {
                vo.setParentName("-");
            } else {
                String parentTitle = parentIdToTitle.get(pid);
                vo.setParentName(parentTitle == null ? "-" : parentTitle);
            }
        }

        return PageResponse.success(page, rspVOS);
    }

    @Override
    public Response createMenu(CreateUmsMenuReqVO reqVO) {
        UmsMenuDO menuDO = UmsMenuDO.builder()
                .parentId(reqVO.getParentId())
                .title(reqVO.getName())
                .name(reqVO.getName())
                .path(reqVO.getPath())
                .icon(reqVO.getIcon())
                .sort(reqVO.getSort())
                .hidden(reqVO.getHidden())
                .createTime(new Date())
                .build();

        menuMapper.insert(menuDO);
        return Response.success();
    }

    @Override
    public Response updateMenu(UpdateUmsMenuReqVO reqVO) {
        UmsMenuDO menuDO = UmsMenuDO.builder()
                .id(reqVO.getId())
                .parentId(reqVO.getParentId())
                .title(reqVO.getName())
                .name(reqVO.getName())
                .path(reqVO.getPath())
                .icon(reqVO.getIcon())
                .sort(reqVO.getSort())
                .hidden(reqVO.getHidden())
                .build();

        int count = menuMapper.updateById(menuDO);
        return count == 1 ? Response.success() : Response.fail("菜单不存在或更新失败");
    }

    @Override
    public Response deleteMenus(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Response.fail("待删除菜单不能为空");
        }

        // 先删角色菜单关系，避免后续逻辑脏数据
        roleMenuRelationMapper.delete(
                new LambdaQueryWrapper<com.gg.guimall.common.domain.dos.UmsRoleMenuRelationDO>()
                        .in(com.gg.guimall.common.domain.dos.UmsRoleMenuRelationDO::getMenuId, ids)
        );

        // 删除菜单本身
        int count = menuMapper.deleteBatchIds(ids);
        return count > 0 ? Response.success() : Response.fail("删除失败");
    }

    @Override
    public List<MenuOptionRspVO> findMenuOptions() {
        List<UmsMenuDO> topMenus = menuMapper.selectList(new LambdaQueryWrapper<UmsMenuDO>()
                .eq(UmsMenuDO::getParentId, 0L)
                .eq(UmsMenuDO::getHidden, 0));

        return topMenus.stream().map(m -> {
            MenuOptionRspVO vo = new MenuOptionRspVO();
            vo.setId(m.getId());
            vo.setName(m.getTitle());
            return vo;
        }).collect(Collectors.toList());
    }
}

