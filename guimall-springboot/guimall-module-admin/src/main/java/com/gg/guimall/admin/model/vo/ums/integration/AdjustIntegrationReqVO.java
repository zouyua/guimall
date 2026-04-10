package com.gg.guimall.admin.model.vo.ums.integration;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 管理员调整积分请求 VO
 */
@Data
@NoArgsConstructor
@ApiModel("管理员调整积分请求 VO")
public class AdjustIntegrationReqVO {
    /** 会员ID */
    @NotNull(message = "会员ID不能为空")
    private Long memberId;

    /**
     * 调整数量（正数增加，负数扣减）
     */
    @NotNull(message = "调整数量不能为空")
    private Integer delta;

    /** 备注说明 */
    private String note;
}
