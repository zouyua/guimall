package com.gg.guimall.admin.model.vo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: wgg
 * @url: www.gg.com
 * @date: 2026/3/10
 * @description: 查询用户信息
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindUserInfoRspVO {
    /*
    * 用户名*/
    private String username;
}
