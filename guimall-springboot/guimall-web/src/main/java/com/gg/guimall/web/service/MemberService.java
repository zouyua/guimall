package com.gg.guimall.web.service;

import com.gg.guimall.common.utils.Response;
import com.gg.guimall.web.model.vo.member.MemberLoginReqVO;
import com.gg.guimall.web.model.vo.member.MemberRegisterReqVO;

public interface MemberService {

    /**
     * 会员注册
     */
    Response register(MemberRegisterReqVO reqVO);

    /**
     * 会员登录
     */
    Response login(MemberLoginReqVO reqVO);

    /**
     * 获取会员信息
     */
    Response getMemberInfo(Long memberId);
}
