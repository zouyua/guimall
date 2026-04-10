package com.gg.guimall.admin.model.vo.ums.memberLevel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 管理员调整会员等级请求 VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdjustMemberLevelReqVO {

    @NotNull(message = "会员ID不能为空")
    private Long memberId;

    @NotNull(message = "等级ID不能为空")
    private Long levelId;

    /** 备注 */
    private String note;
}
