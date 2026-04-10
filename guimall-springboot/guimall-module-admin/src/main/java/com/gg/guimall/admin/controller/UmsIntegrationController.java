package com.gg.guimall.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.admin.model.vo.ums.integration.AdjustIntegrationReqVO;
import com.gg.guimall.admin.model.vo.ums.integration.FindMemberIntegrationPageReqVO;
import com.gg.guimall.common.aspect.ApiOperationLog;
import com.gg.guimall.common.domain.dos.UmsIntegrationHistoryDO;
import com.gg.guimall.common.domain.dos.UmsMemberDO;
import com.gg.guimall.common.domain.mapper.UmsIntegrationHistoryMapper;
import com.gg.guimall.common.domain.mapper.UmsMemberMapper;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 管理端 - 会员积分管理 Controller
 */
@RestController
@RequestMapping("/admin/ums/integration")
@Api(tags = "会员积分管理")
public class UmsIntegrationController {

    @Autowired
    private UmsMemberMapper umsMemberMapper;

    @Autowired
    private UmsIntegrationHistoryMapper integrationHistoryMapper;

    /**
     * 会员积分列表（分页 + 搜索）
     */
    @GetMapping("/memberList")
    @ApiOperation("会员积分列表")
    @ApiOperationLog(description = "会员积分列表")
    public PageResponse memberList(FindMemberIntegrationPageReqVO reqVO) {
        LambdaQueryWrapper<UmsMemberDO> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(reqVO.getUsername())) {
            wrapper.like(UmsMemberDO::getUsername, reqVO.getUsername());
        }
        if (StringUtils.isNotBlank(reqVO.getNickname())) {
            wrapper.like(UmsMemberDO::getNickname, reqVO.getNickname());
        }
        wrapper.orderByDesc(UmsMemberDO::getId);

        Page<UmsMemberDO> page = umsMemberMapper.selectPage(
                new Page<>(reqVO.getCurrent(), reqVO.getSize()), wrapper);
        return PageResponse.success(page, page.getRecords());
    }

    /**
     * 查看指定会员的积分变动历史
     */
    @GetMapping("/history")
    @ApiOperation("积分变动历史")
    @ApiOperationLog(description = "积分变动历史")
    public PageResponse history(@RequestParam Long memberId,
                                @RequestParam(defaultValue = "1") Long current,
                                @RequestParam(defaultValue = "10") Long size,
                                @RequestParam(required = false) Integer changeType) {
        Page<UmsIntegrationHistoryDO> page = integrationHistoryMapper.selectPageList(current, size, memberId, changeType);
        return PageResponse.success(page, page.getRecords());
    }

    /**
     * 管理员手动调整积分
     */
    @PostMapping("/adjust")
    @ApiOperation("手动调整积分")
    @ApiOperationLog(description = "手动调整积分")
    public Response adjust(@RequestBody @Validated AdjustIntegrationReqVO reqVO) {
        Long memberId = reqVO.getMemberId();
        int delta = reqVO.getDelta();

        if (delta == 0) {
            return Response.fail("调整数量不能为0");
        }

        // 查询会员是否存在
        UmsMemberDO member = umsMemberMapper.selectById(memberId);
        if (Objects.isNull(member)) {
            return Response.fail("会员不存在");
        }

        if (delta > 0) {
            // 增加积分
            umsMemberMapper.addIntegration(memberId, delta);
        } else {
            // 扣减积分
            int absDelta = Math.abs(delta);
            if (member.getIntegration() == null || member.getIntegration() < absDelta) {
                return Response.fail("该会员积分不足，当前积分：" + (member.getIntegration() == null ? 0 : member.getIntegration()));
            }
            int rows = umsMemberMapper.deductIntegration(memberId, absDelta);
            if (rows == 0) {
                return Response.fail("积分扣减失败，可能余额不足");
            }
        }

        // 记录积分历史
        UmsIntegrationHistoryDO historyDO = UmsIntegrationHistoryDO.builder()
                .memberId(memberId)
                .changeCount(Math.abs(delta))
                .changeType(delta > 0 ? 0 : 1)  // 0=获取 1=消费
                .sourceType(2)  // 管理员调整
                .note(reqVO.getNote() != null ? reqVO.getNote() : (delta > 0 ? "管理员手动增加积分" : "管理员手动扣减积分"))
                .createTime(LocalDateTime.now())
                .build();
        integrationHistoryMapper.insert(historyDO);

        return Response.success();
    }
}
