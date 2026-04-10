package com.gg.guimall.admin.model.vo.ums.integration;

import com.gg.guimall.common.model.BasePageQuery;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会员积分分页查询请求 VO
 */
@Data
@NoArgsConstructor
@ApiModel("会员积分分页查询请求 VO")
public class FindMemberIntegrationPageReqVO extends BasePageQuery {
    /** 按用户名搜索 */
    private String username;
    /** 按昵称搜索 */
    private String nickname;
}
