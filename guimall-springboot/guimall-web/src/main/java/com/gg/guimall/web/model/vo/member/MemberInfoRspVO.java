package com.gg.guimall.web.model.vo.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会员信息响应 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoRspVO {

    private Long id;

    private String username;

    private String nickname;

    private String phone;

    /** 头像URL */
    private String icon;

    /** 性别：0未知 1男 2女 */
    private Integer gender;

    /** 城市 */
    private String city;

    /** 职业 */
    private String job;

    /** 个性签名 */
    private String personalizedSignature;

    /** 当前可用积分 */
    private Integer integration;

    /** 历史累计积分 */
    private Integer historyIntegration;
}
