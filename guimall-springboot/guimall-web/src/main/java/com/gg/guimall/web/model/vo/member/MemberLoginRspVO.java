package com.gg.guimall.web.model.vo.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会员登录响应 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginRspVO {

    /** JWT Token */
    private String token;

    /** 会员ID */
    private Long memberId;

    /** 昵称 */
    private String nickname;

    /** 头像 */
    private String icon;
}
