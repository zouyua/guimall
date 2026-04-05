package com.gg.guimall.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gg.guimall.common.aspect.ApiOperationLog;
import com.gg.guimall.common.domain.dos.UmsMemberDO;
import com.gg.guimall.common.domain.dos.UmsMemberReceiveAddressDO;
import com.gg.guimall.common.domain.mapper.UmsMemberMapper;
import com.gg.guimall.common.domain.mapper.UmsMemberReceiveAddressMapper;
import com.gg.guimall.common.utils.Response;
import com.gg.guimall.web.model.vo.member.MemberLoginReqVO;
import com.gg.guimall.web.model.vo.member.MemberRegisterReqVO;
import com.gg.guimall.web.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
