package com.gg.guimall.common.domain.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 前台会员表 ums_member
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("ums_member")
public class UmsMemberDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long memberLevelId;
    private String username;
    /** 密文密码 */
    private String password;
    private String nickname;
    private String phone;
    private String email;
    /** 0 禁用 1 启用 */
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String icon;
    /** 0 未知 1 男 2 女 */
    private Integer gender;
    private LocalDate birthday;
    private String city;
    private String job;
    private String personalizedSignature;
    /** 0 WEB 1 APP */
    private Integer sourceType;
    private Integer integration;
    private Integer growth;
    /** 剩余抽奖次数 */
    @TableField("luckey_count")
    private Integer luckeyCount;
    private Integer historyIntegration;
}
