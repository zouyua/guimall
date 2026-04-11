package com.gg.guimall.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.admin.model.vo.ums.memberLevel.AdjustMemberLevelReqVO;
import com.gg.guimall.admin.model.vo.ums.memberLevel.FindMemberLevelPageReqVO;
import com.gg.guimall.admin.model.vo.ums.memberLevel.SaveMemberLevelReqVO;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 管理端 - 会员等级管理 Controller
 */
@RestController
@RequestMapping("/admin/ums/memberLevel")
@Api(tags = "会员等级管理")
public class UmsMemberLevelController {

    @Autowired
    private UmsMemberLevelMapper memberLevelMapper;

    @Autowired
    private UmsMemberMapper umsMemberMapper;

    /**
     * 等级列表（分页）
     */
    @GetMapping("/list")
    @ApiOperation("会员等级列表")
    @ApiOperationLog(description = "会员等级列表")
    public PageResponse list(FindMemberLevelPageReqVO reqVO) {
        LambdaQueryWrapper<UmsMemberLevelDO> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(reqVO.getName())) {
            wrapper.like(UmsMemberLevelDO::getName, reqVO.getName());
        }
        if (Objects.nonNull(reqVO.getStatus())) {
            wrapper.eq(UmsMemberLevelDO::getStatus, reqVO.getStatus());
        }
        wrapper.orderByAsc(UmsMemberLevelDO::getLevel);

        Page<UmsMemberLevelDO> page = memberLevelMapper.selectPage(
                new Page<>(reqVO.getCurrent(), reqVO.getSize()), wrapper);
        return PageResponse.success(page, page.getRecords());
    }

    /**
     * 查询所有启用的等级（不分页，供下拉选择用）
     */
    @GetMapping("/allEnabled")
    @ApiOperation("所有启用的等级")
    @ApiOperationLog(description = "查询所有启用的等级")
    public Response allEnabled() {
        LambdaQueryWrapper<UmsMemberLevelDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UmsMemberLevelDO::getStatus, 1)
               .orderByAsc(UmsMemberLevelDO::getLevel);
        List<UmsMemberLevelDO> list = memberLevelMapper.selectList(wrapper);
        return Response.success(list);
    }

    /**
     * 新增等级
     */
    @PostMapping("/add")
    @ApiOperation("新增等级")
    @ApiOperationLog(description = "新增等级")
    public Response add(@RequestBody @Validated SaveMemberLevelReqVO reqVO) {
        // 校验等级序号唯一性
        LambdaQueryWrapper<UmsMemberLevelDO> checkWrapper = new LambdaQueryWrapper<>();
        checkWrapper.eq(UmsMemberLevelDO::getLevel, reqVO.getLevel());
        if (memberLevelMapper.selectCount(checkWrapper) > 0) {
            return Response.fail("等级序号已存在，请选择其他序号");
        }

        // 折扣必须在 1~100 之间
        if (reqVO.getDiscount() < 1 || reqVO.getDiscount() > 100) {
            return Response.fail("折扣率必须在1~100之间");
        }

        UmsMemberLevelDO levelDO = UmsMemberLevelDO.builder()
                .name(reqVO.getName())
                .level(reqVO.getLevel())
                .price(reqVO.getPrice())
                .discount(reqVO.getDiscount())
                .note(reqVO.getNote())
                .defaultStatus(reqVO.getDefaultStatus() != null ? reqVO.getDefaultStatus() : 0)
                .status(reqVO.getStatus() != null ? reqVO.getStatus() : 1)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();

        // 如果设为默认，先把其他的默认状态取消
        if (Objects.equals(levelDO.getDefaultStatus(), 1)) {
            clearDefaultStatus();
        }

        memberLevelMapper.insert(levelDO);
        return Response.success();
    }

    /**
     * 编辑等级
     */
    @PostMapping("/update")
    @ApiOperation("编辑等级")
    @ApiOperationLog(description = "编辑等级")
    public Response update(@RequestBody @Validated SaveMemberLevelReqVO reqVO) {
        if (Objects.isNull(reqVO.getId())) {
            return Response.fail("等级ID不能为空");
        }

        UmsMemberLevelDO existing = memberLevelMapper.selectById(reqVO.getId());
        if (Objects.isNull(existing)) {
            return Response.fail("等级不存在");
        }

        // 校验等级序号唯一性（排除自身）
        LambdaQueryWrapper<UmsMemberLevelDO> checkWrapper = new LambdaQueryWrapper<>();
        checkWrapper.eq(UmsMemberLevelDO::getLevel, reqVO.getLevel())
                    .ne(UmsMemberLevelDO::getId, reqVO.getId());
        if (memberLevelMapper.selectCount(checkWrapper) > 0) {
            return Response.fail("等级序号已被其他等级使用");
        }

        if (reqVO.getDiscount() < 1 || reqVO.getDiscount() > 100) {
            return Response.fail("折扣率必须在1~100之间");
        }

        existing.setName(reqVO.getName());
        existing.setLevel(reqVO.getLevel());
        existing.setPrice(reqVO.getPrice());
        existing.setDiscount(reqVO.getDiscount());
        existing.setNote(reqVO.getNote());
        existing.setUpdateTime(LocalDateTime.now());

        if (Objects.nonNull(reqVO.getDefaultStatus())) {
            if (Objects.equals(reqVO.getDefaultStatus(), 1) && !Objects.equals(existing.getDefaultStatus(), 1)) {
                clearDefaultStatus();
            }
            existing.setDefaultStatus(reqVO.getDefaultStatus());
        }
        if (Objects.nonNull(reqVO.getStatus())) {
            existing.setStatus(reqVO.getStatus());
        }

        memberLevelMapper.updateById(existing);
        return Response.success();
    }

    /**
     * 删除等级
     */
    @PostMapping("/delete/{id}")
    @ApiOperation("删除等级")
    @ApiOperationLog(description = "删除等级")
    public Response delete(@PathVariable Long id) {
        UmsMemberLevelDO existing = memberLevelMapper.selectById(id);
        if (Objects.isNull(existing)) {
            return Response.fail("等级不存在");
        }
        if (Objects.equals(existing.getDefaultStatus(), 1)) {
            return Response.fail("默认等级不能删除");
        }

        // 检查是否有会员正在使用该等级
        LambdaQueryWrapper<UmsMemberDO> memberWrapper = new LambdaQueryWrapper<>();
        memberWrapper.eq(UmsMemberDO::getMemberLevelId, id);
        Long memberCount = umsMemberMapper.selectCount(memberWrapper);
        if (memberCount > 0) {
            return Response.fail("该等级下还有 " + memberCount + " 个会员，请先调整这些会员的等级");
        }

        memberLevelMapper.deleteById(id);
        return Response.success();
    }

    /**
     * 管理员手动调整会员的等级
     */
    @PostMapping("/adjustMemberLevel")
    @ApiOperation("调整会员等级")
    @ApiOperationLog(description = "调整会员等级")
    public Response adjustMemberLevel(@RequestBody @Validated AdjustMemberLevelReqVO reqVO) {
        UmsMemberDO member = umsMemberMapper.selectById(reqVO.getMemberId());
        if (Objects.isNull(member)) {
            return Response.fail("会员不存在");
        }

        UmsMemberLevelDO level = memberLevelMapper.selectById(reqVO.getLevelId());
        if (Objects.isNull(level)) {
            return Response.fail("目标等级不存在");
        }
        if (!Objects.equals(level.getStatus(), 1)) {
            return Response.fail("目标等级已禁用");
        }

        member.setMemberLevelId(reqVO.getLevelId());
        member.setUpdateTime(LocalDateTime.now());
        umsMemberMapper.updateById(member);

        return Response.success();
    }

    /**
     * 查看某个会员的当前等级信息
     */
    @GetMapping("/memberInfo")
    @ApiOperation("查看会员等级信息")
    @ApiOperationLog(description = "查看会员等级信息")
    public Response memberInfo(@RequestParam Long memberId) {
        UmsMemberDO member = umsMemberMapper.selectById(memberId);
        if (Objects.isNull(member)) {
            return Response.fail("会员不存在");
        }

        UmsMemberLevelDO level = null;
        if (Objects.nonNull(member.getMemberLevelId())) {
            level = memberLevelMapper.selectById(member.getMemberLevelId());
        }
        return Response.success(level);
    }

    /**
     * 清除所有等级的默认标记
     */
    private void clearDefaultStatus() {
        LambdaQueryWrapper<UmsMemberLevelDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UmsMemberLevelDO::getDefaultStatus, 1);
        List<UmsMemberLevelDO> defaults = memberLevelMapper.selectList(wrapper);
        for (UmsMemberLevelDO d : defaults) {
            d.setDefaultStatus(0);
            d.setUpdateTime(LocalDateTime.now());
            memberLevelMapper.updateById(d);
        }
    }
}
