package com.gg.guimall.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.common.aspect.ApiOperationLog;
import com.gg.guimall.common.domain.dos.UmsMemberDO;
import com.gg.guimall.common.domain.dos.UmsMemberLevelDO;
import com.gg.guimall.common.domain.mapper.UmsMemberLevelMapper;
import com.gg.guimall.common.domain.mapper.UmsMemberMapper;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 管理端 - 会员管理 Controller
 */
@RestController
@RequestMapping("/admin/ums/member")
@Api(tags = "会员管理")
public class UmsMemberController {

    @Autowired
    private UmsMemberMapper umsMemberMapper;

    @Autowired
    private UmsMemberLevelMapper memberLevelMapper;

    /**
     * 会员列表（分页 + 搜索）
     */
    @GetMapping("/list")
    @ApiOperation("会员列表")
    @ApiOperationLog(description = "会员列表")
    public PageResponse list(
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long memberLevelId) {

        LambdaQueryWrapper<UmsMemberDO> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            wrapper.like(UmsMemberDO::getUsername, username);
        }
        if (StringUtils.isNotBlank(nickname)) {
            wrapper.like(UmsMemberDO::getNickname, nickname);
        }
        if (StringUtils.isNotBlank(phone)) {
            wrapper.like(UmsMemberDO::getPhone, phone);
        }
        if (Objects.nonNull(status)) {
            wrapper.eq(UmsMemberDO::getStatus, status);
        }
        if (Objects.nonNull(memberLevelId)) {
            wrapper.eq(UmsMemberDO::getMemberLevelId, memberLevelId);
        }
        wrapper.orderByDesc(UmsMemberDO::getCreateTime);

        Page<UmsMemberDO> page = umsMemberMapper.selectPage(new Page<>(current, size), wrapper);

        // 查询等级名称，组装到结果中
        List<UmsMemberDO> records = page.getRecords();
        if (!records.isEmpty()) {
            Set<Long> levelIds = records.stream()
                    .map(UmsMemberDO::getMemberLevelId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            Map<Long, String> levelNameMap = new HashMap<>();
            if (!levelIds.isEmpty()) {
                List<UmsMemberLevelDO> levels = memberLevelMapper.selectBatchIds(levelIds);
                for (UmsMemberLevelDO lv : levels) {
                    levelNameMap.put(lv.getId(), lv.getName());
                }
            }
            // 将等级名称放到 personalizedSignature 的临时字段中传递（避免新增VO）
            // 改用 Map 包装
            List<Map<String, Object>> resultList = new ArrayList<>();
            for (UmsMemberDO m : records) {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("id", m.getId());
                map.put("username", m.getUsername());
                map.put("nickname", m.getNickname());
                map.put("phone", m.getPhone());
                map.put("email", m.getEmail());
                map.put("icon", m.getIcon());
                map.put("gender", m.getGender());
                map.put("birthday", m.getBirthday());
                map.put("city", m.getCity());
                map.put("job", m.getJob());
                map.put("personalizedSignature", m.getPersonalizedSignature());
                map.put("status", m.getStatus());
                map.put("memberLevelId", m.getMemberLevelId());
                map.put("memberLevelName", m.getMemberLevelId() != null ? levelNameMap.getOrDefault(m.getMemberLevelId(), "未知") : "无");
                map.put("integration", m.getIntegration());
                map.put("growth", m.getGrowth());
                map.put("sourceType", m.getSourceType());
                map.put("createTime", m.getCreateTime());
                map.put("updateTime", m.getUpdateTime());
                resultList.add(map);
            }
            return PageResponse.success(page, resultList);
        }

        return PageResponse.success(page, records);
    }

    /**
     * 获取会员详情
     */
    @GetMapping("/detail/{id}")
    @ApiOperation("会员详情")
    @ApiOperationLog(description = "会员详情")
    public Response detail(@PathVariable Long id) {
        UmsMemberDO member = umsMemberMapper.selectById(id);
        if (Objects.isNull(member)) {
            return Response.fail("会员不存在");
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", member.getId());
        result.put("username", member.getUsername());
        result.put("nickname", member.getNickname());
        result.put("phone", member.getPhone());
        result.put("email", member.getEmail());
        result.put("icon", member.getIcon());
        result.put("gender", member.getGender());
        result.put("birthday", member.getBirthday());
        result.put("city", member.getCity());
        result.put("job", member.getJob());
        result.put("personalizedSignature", member.getPersonalizedSignature());
        result.put("status", member.getStatus());
        result.put("memberLevelId", member.getMemberLevelId());
        result.put("integration", member.getIntegration());
        result.put("growth", member.getGrowth());
        result.put("sourceType", member.getSourceType());
        result.put("createTime", member.getCreateTime());
        result.put("updateTime", member.getUpdateTime());

        if (member.getMemberLevelId() != null) {
            UmsMemberLevelDO level = memberLevelMapper.selectById(member.getMemberLevelId());
            result.put("memberLevelName", level != null ? level.getName() : "未知");
        } else {
            result.put("memberLevelName", "无");
        }

        return Response.success(result);
    }

    /**
     * 更新会员信息（含等级、状态）
     */
    @PostMapping("/update")
    @ApiOperation("更新会员信息")
    @ApiOperationLog(description = "更新会员信息")
    public Response update(@RequestBody Map<String, Object> body) {
        Object idObj = body.get("id");
        if (idObj == null) {
            return Response.fail("会员ID不能为空");
        }
        Long id = Long.valueOf(idObj.toString());

        UmsMemberDO existing = umsMemberMapper.selectById(id);
        if (Objects.isNull(existing)) {
            return Response.fail("会员不存在");
        }

        UmsMemberDO update = new UmsMemberDO();
        update.setId(id);

        if (body.containsKey("nickname")) {
            update.setNickname((String) body.get("nickname"));
        }
        if (body.containsKey("phone")) {
            update.setPhone((String) body.get("phone"));
        }
        if (body.containsKey("email")) {
            update.setEmail((String) body.get("email"));
        }
        if (body.containsKey("gender")) {
            update.setGender(body.get("gender") != null ? Integer.valueOf(body.get("gender").toString()) : null);
        }
        if (body.containsKey("city")) {
            update.setCity((String) body.get("city"));
        }
        if (body.containsKey("job")) {
            update.setJob((String) body.get("job"));
        }
        if (body.containsKey("status")) {
            update.setStatus(Integer.valueOf(body.get("status").toString()));
        }
        if (body.containsKey("memberLevelId")) {
            Object levelIdObj = body.get("memberLevelId");
            if (levelIdObj != null && !levelIdObj.toString().isEmpty()) {
                Long levelId = Long.valueOf(levelIdObj.toString());
                UmsMemberLevelDO level = memberLevelMapper.selectById(levelId);
                if (level == null || level.getStatus() != 1) {
                    return Response.fail("目标等级不存在或已禁用");
                }
                update.setMemberLevelId(levelId);
            } else {
                update.setMemberLevelId(null);
            }
        }

        update.setUpdateTime(LocalDateTime.now());
        umsMemberMapper.updateById(update);
        return Response.success();
    }

    /**
     * 切换会员状态（启用/禁用）
     */
    @PostMapping("/toggleStatus/{id}")
    @ApiOperation("切换会员状态")
    @ApiOperationLog(description = "切换会员状态")
    public Response toggleStatus(@PathVariable Long id) {
        UmsMemberDO member = umsMemberMapper.selectById(id);
        if (Objects.isNull(member)) {
            return Response.fail("会员不存在");
        }

        UmsMemberDO update = new UmsMemberDO();
        update.setId(id);
        update.setStatus(member.getStatus() == 1 ? 0 : 1);
        update.setUpdateTime(LocalDateTime.now());
        umsMemberMapper.updateById(update);
        return Response.success();
    }
}
