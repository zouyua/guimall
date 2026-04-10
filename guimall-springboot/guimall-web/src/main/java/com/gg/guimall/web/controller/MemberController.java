package com.gg.guimall.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.common.aspect.ApiOperationLog;
import com.gg.guimall.common.domain.dos.UmsIntegrationHistoryDO;
import com.gg.guimall.common.domain.dos.UmsMemberDO;
import com.gg.guimall.common.domain.dos.UmsMemberLevelDO;
import com.gg.guimall.common.domain.dos.UmsMemberReceiveAddressDO;
import com.gg.guimall.common.domain.dos.OmsOrderDO;
import com.gg.guimall.common.domain.mapper.OmsOrderMapper;
import com.gg.guimall.common.domain.mapper.UmsIntegrationHistoryMapper;
import com.gg.guimall.common.domain.mapper.UmsMemberLevelMapper;
import com.gg.guimall.common.domain.mapper.UmsMemberMapper;
import com.gg.guimall.common.domain.mapper.UmsMemberReceiveAddressMapper;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;
import com.gg.guimall.web.model.vo.member.MemberLoginReqVO;
import com.gg.guimall.web.model.vo.member.MemberRegisterReqVO;
import com.gg.guimall.web.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 前台会员 Controller
 */
@RestController
@RequestMapping("/member")
@Api(tags = "前台会员")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private UmsMemberMapper umsMemberMapper;

    @Autowired
    private UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;

    @Autowired
    private UmsIntegrationHistoryMapper integrationHistoryMapper;

    @Autowired
    private UmsMemberLevelMapper memberLevelMapper;

    @Autowired
    private OmsOrderMapper omsOrderMapper;

    @PostMapping("/register")
    @ApiOperation(value = "会员注册")
    @ApiOperationLog(description = "会员注册")
    public Response register(@RequestBody @Validated MemberRegisterReqVO reqVO) {
        return memberService.register(reqVO);
    }

    @PostMapping("/login")
    @ApiOperation(value = "会员登录")
    @ApiOperationLog(description = "会员登录")
    public Response login(@RequestBody @Validated MemberLoginReqVO reqVO) {
        return memberService.login(reqVO);
    }

    @GetMapping("/info")
    @ApiOperation(value = "获取会员信息")
    @ApiOperationLog(description = "获取会员信息")
    public Response info(@RequestParam Long memberId) {
        return memberService.getMemberInfo(memberId);
    }

    @PostMapping("/update")
    @ApiOperation("更新会员资料")
    public Response updateInfo(@RequestBody UmsMemberDO memberDO) {
        if (memberDO.getId() == null) {
            return Response.fail("会员ID不能为空");
        }
        // Only allow updating safe fields
        UmsMemberDO update = new UmsMemberDO();
        update.setId(memberDO.getId());
        update.setNickname(memberDO.getNickname());
        update.setPhone(memberDO.getPhone());
        update.setIcon(memberDO.getIcon());
        update.setGender(memberDO.getGender());
        update.setBirthday(memberDO.getBirthday());
        update.setCity(memberDO.getCity());
        update.setJob(memberDO.getJob());
        update.setPersonalizedSignature(memberDO.getPersonalizedSignature());
        umsMemberMapper.updateById(update);
        return Response.success();
    }

    @GetMapping("/address/list")
    @ApiOperation("收货地址列表")
    public Response addressList(@RequestParam Long memberId) {
        List<UmsMemberReceiveAddressDO> list = umsMemberReceiveAddressMapper.selectByMemberId(memberId);
        return Response.success(list);
    }

    @PostMapping("/address/add")
    @ApiOperation("新增收货地址")
    public Response addAddress(@RequestBody UmsMemberReceiveAddressDO addressDO) {
        addressDO.setId(null);
        umsMemberReceiveAddressMapper.insert(addressDO);
        return Response.success();
    }

    @PostMapping("/address/update")
    @ApiOperation("修改收货地址")
    public Response updateAddress(@RequestBody UmsMemberReceiveAddressDO addressDO) {
        if (addressDO.getId() == null) {
            return Response.fail("地址ID不能为空");
        }
        umsMemberReceiveAddressMapper.updateById(addressDO);
        return Response.success();
    }

    @DeleteMapping("/address/{id}")
    @ApiOperation("删除收货地址")
    public Response deleteAddress(@PathVariable Long id) {
        umsMemberReceiveAddressMapper.deleteById(id);
        return Response.success();
    }

    @PostMapping("/address/{id}/default")
    @ApiOperation("设为默认地址")
    public Response setDefaultAddress(@PathVariable Long id, @RequestParam Long memberId) {
        // Clear all default addresses for this member
        umsMemberReceiveAddressMapper.update(null,
                new LambdaUpdateWrapper<UmsMemberReceiveAddressDO>()
                        .eq(UmsMemberReceiveAddressDO::getMemberId, memberId)
                        .set(UmsMemberReceiveAddressDO::getIsDefault, 0));
        // Set the specified address as default
        UmsMemberReceiveAddressDO update = new UmsMemberReceiveAddressDO();
        update.setId(id);
        update.setIsDefault(1);
        umsMemberReceiveAddressMapper.updateById(update);
        return Response.success();
    }

    @GetMapping("/integration/history")
    @ApiOperation("积分变动历史（分页）")
    public PageResponse integrationHistory(
            @RequestParam Long memberId,
            @RequestParam(defaultValue = "1") Long current,
            @RequestParam(defaultValue = "10") Long size,
            @RequestParam(required = false) Integer changeType) {
        Page<UmsIntegrationHistoryDO> page = integrationHistoryMapper.selectPageList(current, size, memberId, changeType);
        return PageResponse.success(page, page.getRecords());
    }

    // ==================== 会员等级相关 ====================

    /**
     * 获取所有可用等级列表（前台展示用）
     */
    @GetMapping("/level/list")
    @ApiOperation("会员等级列表")
    @ApiOperationLog(description = "获取会员等级列表")
    public Response levelList() {
        LambdaQueryWrapper<UmsMemberLevelDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UmsMemberLevelDO::getStatus, 1)
               .orderByAsc(UmsMemberLevelDO::getLevel);
        return Response.success(memberLevelMapper.selectList(wrapper));
    }

    /**
     * 获取当前会员的等级信息
     */
    @GetMapping("/level/current")
    @ApiOperation("获取当前会员等级")
    @ApiOperationLog(description = "获取当前会员等级信息")
    public Response currentLevel(@RequestParam Long memberId) {
        UmsMemberDO member = umsMemberMapper.selectById(memberId);
        if (member == null) {
            return Response.fail("会员不存在");
        }
        // 会员被禁用时不返回等级信息，不享受折扣
        if (member.getStatus() != null && member.getStatus() == 0) {
            return Response.success(null);
        }
        UmsMemberLevelDO level = null;
        if (member.getMemberLevelId() != null) {
            level = memberLevelMapper.selectById(member.getMemberLevelId());
            // 等级被禁用时也不返回
            if (level != null && level.getStatus() != null && level.getStatus() == 0) {
                level = null;
            }
        }
        return Response.success(level);
    }

    /**
     * 开通/升级会员等级（创建订单 → 走支付流程）
     */
    @PostMapping("/level/purchase")
    @ApiOperation("开通/升级会员等级")
    @ApiOperationLog(description = "开通/升级会员等级")
    public Response purchaseLevel(@RequestParam Long memberId, @RequestParam Long levelId) {
        UmsMemberDO member = umsMemberMapper.selectById(memberId);
        if (member == null) {
            return Response.fail("会员不存在");
        }

        UmsMemberLevelDO targetLevel = memberLevelMapper.selectById(levelId);
        if (targetLevel == null || targetLevel.getStatus() != 1) {
            return Response.fail("目标等级不存在或已禁用");
        }

        // 获取当前等级
        UmsMemberLevelDO currentLevel = null;
        if (member.getMemberLevelId() != null) {
            currentLevel = memberLevelMapper.selectById(member.getMemberLevelId());
        }

        // 不能降级
        if (currentLevel != null && currentLevel.getLevel() >= targetLevel.getLevel()) {
            return Response.fail("当前等级已达到或超过目标等级，无法降级");
        }

        // 创建会员等级订单
        String orderSn = "VIP" + System.currentTimeMillis() + "_" + memberId;
        BigDecimal payAmount = targetLevel.getPrice();

        OmsOrderDO order = new OmsOrderDO();
        order.setMemberId(memberId);
        order.setOrderSn(orderSn);
        order.setMemberUsername(member.getUsername());
        order.setTotalAmount(payAmount);
        order.setPayAmount(payAmount);
        order.setFreightAmount(BigDecimal.ZERO);
        order.setPromotionAmount(BigDecimal.ZERO);
        order.setCouponAmount(BigDecimal.ZERO);
        order.setIntegrationAmount(BigDecimal.ZERO);
        order.setPayType(0);
        order.setSourceType(0);
        order.setStatus(0); // 待付款
        order.setOrderType(1); // 会员等级订单
        order.setNote("开通会员等级：" + targetLevel.getName() + "（等级ID：" + levelId + "）");
        // 会员等级订单无需收货信息，填充默认值避免 NOT NULL 约束报错
        order.setReceiverName(member.getNickname() != null ? member.getNickname() : member.getUsername());
        order.setReceiverPhone(member.getPhone() != null ? member.getPhone() : "");
        order.setReceiverPostCode("");
        order.setReceiverProvince("");
        order.setReceiverCity("");
        order.setReceiverRegion("");
        order.setReceiverDetailAddress("");
        order.setConfirmStatus(0);
        order.setIsDeleted(0);
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        omsOrderMapper.insert(order);

        // 返回订单号和金额给前端，前端跳转支付页
        Map<String, Object> result = new HashMap<>();
        result.put("orderSn", orderSn);
        result.put("payAmount", payAmount);
        return Response.success(result);
    }
}
