package com.gg.guimall.web.service.impl;

import com.gg.guimall.common.domain.dos.UmsMemberDO;
import com.gg.guimall.common.domain.mapper.UmsMemberMapper;
import com.gg.guimall.common.utils.Response;
import com.gg.guimall.jwt.utils.JwtTokenHelper;
import com.gg.guimall.web.model.vo.member.MemberInfoRspVO;
import com.gg.guimall.web.model.vo.member.MemberLoginReqVO;
import com.gg.guimall.web.model.vo.member.MemberLoginRspVO;
import com.gg.guimall.web.model.vo.member.MemberRegisterReqVO;
import com.gg.guimall.web.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 前台会员服务实现类
 */
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private UmsMemberMapper umsMemberMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Override
    public Response register(MemberRegisterReqVO reqVO) {
        // 检查用户名是否已存在
        UmsMemberDO existMember = umsMemberMapper.selectByUsername(reqVO.getUsername());
        if (Objects.nonNull(existMember)) {
            return Response.fail("该用户名已被注册");
        }

        // 如果提供了手机号，检查手机号是否已存在
        if (Objects.nonNull(reqVO.getPhone()) && !reqVO.getPhone().isEmpty()) {
            UmsMemberDO phoneMember = umsMemberMapper.selectByPhone(reqVO.getPhone());
            if (Objects.nonNull(phoneMember)) {
                return Response.fail("该手机号已被注册");
            }
        }

        // 创建会员
        UmsMemberDO memberDO = UmsMemberDO.builder()
                .username(reqVO.getUsername())
                .password(passwordEncoder.encode(reqVO.getPassword()))
                .nickname(Objects.nonNull(reqVO.getNickname()) ? reqVO.getNickname() : reqVO.getUsername())
                .phone(reqVO.getPhone())
                .status(1)
                .sourceType(0)
                .integration(0)
                .growth(0)
                .historyIntegration(0)
                .luckeyCount(0)
                .createTime(LocalDateTime.now())
                .build();

        umsMemberMapper.insert(memberDO);

        return Response.success();
    }

    @Override
    public Response login(MemberLoginReqVO reqVO) {
        // 根据用户名查找会员
        UmsMemberDO memberDO = umsMemberMapper.selectByUsername(reqVO.getUsername());
        if (Objects.isNull(memberDO)) {
            return Response.fail("用户名或密码错误");
        }

        // 检查账号状态
        if (!Objects.equals(memberDO.getStatus(), 1)) {
            return Response.fail("该账号已被禁用");
        }

        // 验证密码
        if (!passwordEncoder.matches(reqVO.getPassword(), memberDO.getPassword())) {
            return Response.fail("用户名或密码错误");
        }

        // 生成 JWT Token，subject 使用 memberId
        String token = jwtTokenHelper.generateToken(memberDO.getId().toString());

        // 返回登录信息（使用 VO 而非 Map）
        MemberLoginRspVO rspVO = MemberLoginRspVO.builder()
                .token(token)
                .memberId(memberDO.getId())
                .nickname(memberDO.getNickname())
                .icon(memberDO.getIcon())
                .build();

        return Response.success(rspVO);
    }

    @Override
    public Response getMemberInfo(Long memberId) {
        if (Objects.isNull(memberId) || memberId <= 0) {
            return Response.fail("会员ID无效");
        }

        UmsMemberDO memberDO = umsMemberMapper.selectById(memberId);
        if (Objects.isNull(memberDO)) {
            return Response.fail("会员不存在");
        }

        // 返回会员基本信息（使用 VO，不暴露密码等敏感字段）
        MemberInfoRspVO rspVO = MemberInfoRspVO.builder()
                .id(memberDO.getId())
                .username(memberDO.getUsername())
                .nickname(memberDO.getNickname())
                .phone(memberDO.getPhone())
                .icon(memberDO.getIcon())
                .gender(memberDO.getGender())
                .city(memberDO.getCity())
                .job(memberDO.getJob())
                .personalizedSignature(memberDO.getPersonalizedSignature())
                .integration(memberDO.getIntegration())
                .historyIntegration(memberDO.getHistoryIntegration())
                .build();

        return Response.success(rspVO);
    }
}
