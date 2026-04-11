package com.gg.guimall.admin.model.vo.ums.memberLevel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 新增/编辑会员等级请求 VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveMemberLevelReqVO {
    /** 编辑时传id，新增时为null */
    private Long id;

    @NotBlank(message = "等级名称不能为空")
    private String name;

    @NotNull(message = "等级序号不能为空")
    private Integer level;

    @NotNull(message = "开通价格不能为空")
    private BigDecimal price;

    @NotNull(message = "折扣率不能为空")
    private Integer discount;

    /** 等级说明 */
    private String note;

    /** 是否为默认等级 0否1是 */
    private Integer defaultStatus;

    /** 状态 0禁用 1启用 */
    private Integer status;
}
