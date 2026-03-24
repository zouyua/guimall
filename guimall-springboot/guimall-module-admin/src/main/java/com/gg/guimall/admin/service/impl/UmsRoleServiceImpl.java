package com.gg.guimall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gg.guimall.admin.model.vo.ums.menu.MenuOptionRspVO;
import com.gg.guimall.admin.model.vo.ums.role.*;
import com.gg.guimall.admin.service.UmsRoleService;
import com.gg.guimall.common.domain.dos.UmsMenuDO;
import com.gg.guimall.common.domain.dos.UmsRoleMenuRelationDO;
import com.gg.guimall.common.domain.dos.UserRoleDO;
import com.gg.guimall.common.domain.mapper.UmsMenuMapper;
import com.gg.guimall.common.domain.mapper.UmsRoleMenuRelationMapper;
import com.gg.guimall.common.domain.mapper.UserRoleMapper;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UmsRoleServiceImpl implements UmsRoleService {

    private static final List<String> KNOWN_ROLE_ORDER = Arrays.asList(
            "ROLE_ADMIN",
            "ROLE_ORDER",
            "ROLE_PRODUCT",
            "ROLE_MKT",
            "ROLE_VISITOR"
    );

    private static final Map<String, String> ROLE_REMARK_MAP = new HashMap<String, String>() {{
        put("ROLE_ADMIN", "全部后台菜单与操作权限");
        put("ROLE_ORDER", "订单、发货、退货与售后相关");
        put("ROLE_PRODUCT", "商品、分类、农户与产地等");
        put("ROLE_MKT", "优惠券、推荐位与轮播图等");
        put("ROLE_VISITOR", "联调/UAT，仅开放必要菜单");
    }};

    @Autowired
    private UmsMenuMapper menuMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private UmsRoleMenuRelationMapper roleMenuRelationMapper;

    @Override
    public PageResponse<FindUmsRolePageListRspVO> findRolePageList(FindUmsRolePageListReqVO reqVO) {
        List<String> allRoleCodesSorted = getAllRoleCodesSorted();

        Map<String, Integer> roleUserCount = getRoleUserCountMap();

        List<FindUmsRolePageListRspVO> fullRows = new ArrayList<>();
        for (int i = 0; i < allRoleCodesSorted.size(); i++) {
            String code = allRoleCodesSorted.get(i);
            FindUmsRolePageListRspVO vo = new FindUmsRolePageListRspVO();
            vo.setId((long) i + 1);
            vo.setName(code);
            vo.setRemark(ROLE_REMARK_MAP.getOrDefault(code, ""));
            vo.setUserCount(roleUserCount.getOrDefault(code, 0));
            fullRows.add(vo);
        }

        String nameFilter = reqVO.getName() == null ? null : reqVO.getName().trim();
        List<FindUmsRolePageListRspVO> filtered = fullRows;
        if (nameFilter != null && !nameFilter.isEmpty()) {
            filtered = fullRows.stream()
                    .filter(r -> r.getName() != null && r.getName().contains(nameFilter))
                    .collect(Collectors.toList());
        }

        long total = filtered.size();
        long current = reqVO.getCurrent() == null ? 1L : reqVO.getCurrent();
        long size = reqVO.getSize() == null ? 10L : reqVO.getSize();
        long fromIndex = Math.max(0, (current - 1) * size);
        long toIndex = Math.min(total, fromIndex + size);

        List<FindUmsRolePageListRspVO> pageRows = fromIndex >= toIndex ? Collections.emptyList() : filtered.subList((int) fromIndex, (int) toIndex);

        PageResponse<FindUmsRolePageListRspVO> resp = new PageResponse<>();
        resp.setSuccess(true);
        resp.setCurrent(current);
        resp.setSize(size);
        resp.setTotal(total);
        resp.setPages(size <= 0 ? 0 : (total + size - 1) / size);
        resp.setData(pageRows);
        return resp;
    }

    @Override
    public Response createRole(CreateUmsRoleReqVO reqVO) {
        String roleCode = reqVO.getName() == null ? null : reqVO.getName().trim();
        if (roleCode == null || roleCode.isEmpty()) {
            return Response.fail("角色名称不能为空");
        }

        List<String> allRoleCodesSorted = getAllRoleCodesSorted();
        if (allRoleCodesSorted.contains(roleCode)) {
            return Response.fail("角色已存在");
        }

        UmsRoleMenuRelationDO relation = UmsRoleMenuRelationDO.builder()
                .role(roleCode)
                .menuId(null)
                .build();
        roleMenuRelationMapper.insert(relation);

        return Response.success();
    }

    @Override
    public Response updateRole(UpdateUmsRoleReqVO reqVO) {
        if (reqVO.getId() == null) {
            return Response.fail("角色ID不能为空");
        }
        String oldRoleCode = roleCodeById(reqVO.getId());
        if (oldRoleCode == null) {
            return Response.fail("角色不存在");
        }
        if ("ROLE_ADMIN".equals(oldRoleCode)) {
            return Response.fail("超级管理员角色不允许修改");
        }

        String newRoleCode = reqVO.getName() == null ? null : reqVO.getName().trim();
        if (newRoleCode == null || newRoleCode.isEmpty()) {
            return Response.fail("角色名称不能为空");
        }
        List<String> allRoleCodesSorted = getAllRoleCodesSorted();
        if (allRoleCodesSorted.contains(newRoleCode) && !newRoleCode.equals(oldRoleCode)) {
            return Response.fail("角色名称已存在");
        }

        // 更新 t_user_role.role
        userRoleMapper.update(null, new LambdaUpdateWrapper<UserRoleDO>()
                .set(UserRoleDO::getRole, newRoleCode)
                .eq(UserRoleDO::getRole, oldRoleCode));

        // 更新 t_role_menu_relation.role
        roleMenuRelationMapper.update(null, new LambdaUpdateWrapper<UmsRoleMenuRelationDO>()
                .set(UmsRoleMenuRelationDO::getRole, newRoleCode)
                .eq(UmsRoleMenuRelationDO::getRole, oldRoleCode));

        return Response.success();
    }

    @Override
    public Response deleteRoles(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Response.fail("待删除角色不能为空");
        }

        // 保护 ROLE_ADMIN
        for (Long id : ids) {
            if (id == null) continue;
            String code = roleCodeById(id);
            if ("ROLE_ADMIN".equals(code)) {
                return Response.fail("超级管理员不允许删除");
            }
        }

        for (Long id : ids) {
            if (id == null) continue;
            String code = roleCodeById(id);
            if (code == null) continue;

            // 删除角色-用户关系
            userRoleMapper.delete(new LambdaQueryWrapper<UserRoleDO>().eq(UserRoleDO::getRole, code));
            // 删除角色-菜单关系（包含 menuId=null 用于“占位”角色）
            roleMenuRelationMapper.deleteByRole(code);
        }
        return Response.success();
    }

    @Override
    public List<RoleOptionRspVO> findRoleOptions() {
        List<String> roleCodes = getAllRoleCodesSorted();
        List<RoleOptionRspVO> options = new ArrayList<>();
        for (int i = 0; i < roleCodes.size(); i++) {
            RoleOptionRspVO vo = new RoleOptionRspVO();
            vo.setId((long) i + 1);
            vo.setName(roleCodes.get(i));
            options.add(vo);
        }
        return options;
    }

    @Override
    public AllocMenuInitRspVO initAllocMenu(Long roleId) {
        String roleCode = roleCodeById(roleId);
        if (roleCode == null) {
            AllocMenuInitRspVO empty = new AllocMenuInitRspVO();
            empty.setRoleName("");
            empty.setTreeData(Collections.emptyList());
            empty.setCheckedKeys(Collections.emptyList());
            return empty;
        }

        List<UmsMenuDO> menus = menuMapper.selectList(new LambdaQueryWrapper<UmsMenuDO>().eq(UmsMenuDO::getHidden, 0));
        Map<Long, List<UmsMenuDO>> parentIdToMenus = menus.stream().collect(Collectors.groupingBy(m -> Optional.ofNullable(m.getParentId()).orElse(0L)));
        Map<Long, UmsMenuDO> menuById = menus.stream().collect(Collectors.toMap(UmsMenuDO::getId, m -> m, (a, b) -> a));

        List<TreeNodeRspVO> treeData = new ArrayList<>();
        List<UmsMenuDO> roots = parentIdToMenus.getOrDefault(0L, Collections.emptyList());
        for (UmsMenuDO root : roots) {
            treeData.add(buildNode(root, parentIdToMenus, menuById));
        }

        List<UmsRoleMenuRelationDO> relations = roleMenuRelationMapper.selectByRole(roleCode);
        List<String> checkedKeys = relations.stream()
                .map(UmsRoleMenuRelationDO::getMenuId)
                .filter(Objects::nonNull)
                .filter(menuById::containsKey)
                .map(mid -> String.valueOf(mid))
                .distinct()
                .collect(Collectors.toList());

        AllocMenuInitRspVO rspVO = new AllocMenuInitRspVO();
        rspVO.setRoleName(roleCode);
        rspVO.setTreeData(treeData);
        rspVO.setCheckedKeys(checkedKeys);
        return rspVO;
    }

    private TreeNodeRspVO buildNode(UmsMenuDO node,
                                    Map<Long, List<UmsMenuDO>> parentIdToMenus,
                                    Map<Long, UmsMenuDO> menuById) {
        TreeNodeRspVO vo = new TreeNodeRspVO();
        vo.setTitle(node.getTitle());
        vo.setKey(String.valueOf(node.getId()));

        List<UmsMenuDO> children = parentIdToMenus.getOrDefault(node.getId(), Collections.emptyList());
        if (children.isEmpty()) {
            vo.setChildren(Collections.emptyList());
            return vo;
        }

        List<TreeNodeRspVO> childVos = children.stream()
                .map(c -> buildNode(c, parentIdToMenus, menuById))
                .collect(Collectors.toList());
        vo.setChildren(childVos);
        return vo;
    }

    @Override
    public Response saveAllocMenu(SaveUmsRoleAllocMenuReqVO reqVO) {
        String roleCode = roleCodeById(reqVO.getRoleId());
        if (roleCode == null) {
            return Response.fail("角色不存在");
        }

        List<String> checkedKeys = reqVO.getCheckedKeys();
        List<Long> menuIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(checkedKeys)) {
            for (String key : checkedKeys) {
                if (key == null || key.trim().isEmpty()) continue;
                try {
                    menuIds.add(Long.parseLong(key.trim()));
                } catch (Exception ignored) {
                    // 非法 key 忽略
                }
            }
        }

        // 清空原有关系
        roleMenuRelationMapper.deleteByRole(roleCode);

        if (menuIds.isEmpty()) {
            // 保留一个占位，确保角色在列表里可见
            roleMenuRelationMapper.insert(UmsRoleMenuRelationDO.builder()
                    .role(roleCode)
                    .menuId(null)
                    .build());
            return Response.success();
        }

        for (Long menuId : menuIds) {
            roleMenuRelationMapper.insert(UmsRoleMenuRelationDO.builder()
                    .role(roleCode)
                    .menuId(menuId)
                    .build());
        }

        return Response.success();
    }

    private List<String> getAllRoleCodesSorted() {
        List<UserRoleDO> userRoleList = userRoleMapper.selectList(null);
        List<UmsRoleMenuRelationDO> relationList = roleMenuRelationMapper.selectList(null);

        Set<String> codesSet = new HashSet<>();
        if (!CollectionUtils.isEmpty(userRoleList)) {
            for (UserRoleDO r : userRoleList) {
                if (r.getRole() != null && !r.getRole().trim().isEmpty()) {
                    codesSet.add(r.getRole().trim());
                }
            }
        }
        if (!CollectionUtils.isEmpty(relationList)) {
            for (UmsRoleMenuRelationDO r : relationList) {
                if (r.getRole() != null && !r.getRole().trim().isEmpty()) {
                    codesSet.add(r.getRole().trim());
                }
            }
        }

        List<String> result = new ArrayList<>();
        for (String known : KNOWN_ROLE_ORDER) {
            if (codesSet.contains(known)) {
                result.add(known);
            }
        }
        List<String> remaining = codesSet.stream()
                .filter(code -> !KNOWN_ROLE_ORDER.contains(code))
                .sorted()
                .collect(Collectors.toList());
        result.addAll(remaining);
        return result;
    }

    private Map<String, Integer> getRoleUserCountMap() {
        List<UserRoleDO> userRoles = userRoleMapper.selectList(null);
        Map<String, Integer> map = new HashMap<>();
        for (UserRoleDO r : userRoles) {
            if (r.getRole() == null) continue;
            map.put(r.getRole(), map.getOrDefault(r.getRole(), 0) + 1);
        }
        return map;
    }

    private String roleCodeById(Long roleId) {
        if (roleId == null || roleId <= 0) return null;
        List<String> roleCodes = getAllRoleCodesSorted();
        int index = roleId.intValue() - 1;
        if (index < 0 || index >= roleCodes.size()) return null;
        return roleCodes.get(index);
    }
}

