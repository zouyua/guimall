package com.gg.guimall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.admin.model.vo.ums.admin.*;
import com.gg.guimall.admin.service.UmsAdminService;
import com.gg.guimall.admin.service.UmsRoleService;
import com.gg.guimall.admin.model.vo.ums.role.RoleOptionRspVO;
import com.gg.guimall.common.domain.dos.UserDO;
import com.gg.guimall.common.domain.dos.UserRoleDO;
import com.gg.guimall.common.domain.mapper.UserMapper;
import com.gg.guimall.common.domain.mapper.UserRoleMapper;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UmsAdminServiceImpl implements UmsAdminService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private UmsRoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public PageResponse<FindUmsAdminPageListRspVO> findAdminPageList(FindUmsAdminPageListReqVO reqVO) {
        Page<UserDO> page = new Page<>(reqVO.getCurrent(), reqVO.getSize());
        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<>();

        if (reqVO.getUsername() != null && !reqVO.getUsername().trim().isEmpty()) {
            wrapper.like(UserDO::getUsername, reqVO.getUsername().trim());
        }
        if (reqVO.getStatus() != null) {
            boolean isDeleted = reqVO.getStatus() == 0;
            wrapper.eq(UserDO::getIsDeleted, isDeleted);
        }

        IPage<UserDO> resultPage = userMapper.selectPage(page, wrapper);

        List<UserDO> records = resultPage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            PageResponse<FindUmsAdminPageListRspVO> resp = new PageResponse<>();
            resp.setSuccess(true);
            resp.setCurrent(page.getCurrent());
            resp.setSize(page.getSize());
            resp.setTotal(resultPage.getTotal());
            resp.setPages(resultPage.getPages());
            resp.setData(Collections.emptyList());
            return resp;
        }

        // roleId <-> roleCode 映射
        List<RoleOptionRspVO> roleOptions = roleService.findRoleOptions();
        Map<String, Long> roleCodeToId = roleOptions.stream()
                .collect(Collectors.toMap(RoleOptionRspVO::getName, RoleOptionRspVO::getId, (a, b) -> a));

        List<FindUmsAdminPageListRspVO> rspRows = new ArrayList<>();
        for (UserDO u : records) {
            FindUmsAdminPageListRspVO vo = new FindUmsAdminPageListRspVO();
            vo.setId(u.getId());
            vo.setUsername(u.getUsername());
            vo.setNickname(u.getAvatar() == null ? u.getUsername() : u.getAvatar());
            vo.setCreateTime(u.getCreateTime());

            List<UserRoleDO> roles = userRoleMapper.selectByUsername(u.getUsername());
            if (!CollectionUtils.isEmpty(roles)) {
                String roleCode = roles.get(0).getRole();
                vo.setRoleId(roleCodeToId.get(roleCode));
                vo.setRoleName(roleCode);
            }

            // 前端：1 正常 0 禁用
            vo.setStatus(Boolean.TRUE.equals(u.getIsDeleted()) ? 0 : 1);
            rspRows.add(vo);
        }

        return PageResponse.success(page, rspRows);
    }

    @Override
    public Response createAdmin(CreateUmsAdminReqVO reqVO) {
        String username = reqVO.getUsername();
        if (userMapper.findByUsername(username) != null) {
            return Response.fail("登录账号已存在");
        }

        String roleCode = roleCodeById(reqVO.getRoleId());
        if (roleCode == null) {
            return Response.fail("角色不存在");
        }

        LocalDateTime now = LocalDateTime.now();
        UserDO userDO = UserDO.builder()
                .username(reqVO.getUsername())
                .password(passwordEncoder.encode(reqVO.getPassword()))
                .avatar(reqVO.getNickname())
                .createTime(now)
                .updateTime(now)
                .isDeleted(reqVO.getStatus() == 0)
                .build();
        userMapper.insert(userDO);

        userRoleMapper.insert(UserRoleDO.builder()
                .username(reqVO.getUsername())
                .role(roleCode)
                .createTime(now)
                .build());

        return Response.success();
    }

    @Override
    public Response updateAdmin(UpdateUmsAdminReqVO reqVO) {
        UserDO existing = userMapper.selectById(reqVO.getId());
        if (existing == null) {
            return Response.fail("后台账号不存在");
        }

        String roleCode = roleCodeById(reqVO.getRoleId());
        if (roleCode == null) {
            return Response.fail("角色不存在");
        }

        // 更新用户信息（只更新非空字段）
        if (reqVO.getUsername() != null && !reqVO.getUsername().trim().isEmpty()) {
            existing.setUsername(reqVO.getUsername());
        }
        existing.setAvatar(reqVO.getNickname());
        existing.setIsDeleted(reqVO.getStatus() == 0);
        existing.setUpdateTime(LocalDateTime.now());

        if (reqVO.getPassword() != null && !reqVO.getPassword().trim().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(reqVO.getPassword()));
        }

        userMapper.updateById(existing);

        // 重新绑定角色（这里按“单角色”处理：先删再插）
        userRoleMapper.delete(new LambdaQueryWrapper<UserRoleDO>()
                .eq(UserRoleDO::getUsername, existing.getUsername()));
        userRoleMapper.insert(UserRoleDO.builder()
                .username(existing.getUsername())
                .role(roleCode)
                .createTime(LocalDateTime.now())
                .build());

        return Response.success();
    }

    @Override
    public Response deleteAdmins(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Response.fail("待删除后台账号不能为空");
        }

        for (Long id : ids) {
            if (id == null) continue;
            UserDO u = userMapper.selectById(id);
            if (u == null) continue;

            userMapper.update(null, new LambdaUpdateWrapper<UserDO>()
                    .set(UserDO::getIsDeleted, true)
                    .set(UserDO::getUpdateTime, LocalDateTime.now())
                    .eq(UserDO::getId, id));

            userRoleMapper.delete(new LambdaQueryWrapper<UserRoleDO>().eq(UserRoleDO::getUsername, u.getUsername()));
        }

        return Response.success();
    }

    @Override
    public Response assignRole(AssignUmsAdminRoleReqVO reqVO) {
        if (reqVO.getId() == null) {
            return Response.fail("后台账号ID不能为空");
        }

        UserDO u = userMapper.selectById(reqVO.getId());
        if (u == null) {
            return Response.fail("后台账号不存在");
        }

        String roleCode = roleCodeById(reqVO.getRoleId());
        if (roleCode == null) {
            return Response.fail("角色不存在");
        }

        userRoleMapper.delete(new LambdaQueryWrapper<UserRoleDO>()
                .eq(UserRoleDO::getUsername, u.getUsername()));
        userRoleMapper.insert(UserRoleDO.builder()
                .username(u.getUsername())
                .role(roleCode)
                .createTime(LocalDateTime.now())
                .build());

        return Response.success();
    }

    private String roleCodeById(Long roleId) {
        if (roleId == null) return null;
        return roleService.findRoleOptions().stream()
                .filter(o -> roleId.equals(o.getId()))
                .map(RoleOptionRspVO::getName)
                .findFirst()
                .orElse(null);
    }
}

