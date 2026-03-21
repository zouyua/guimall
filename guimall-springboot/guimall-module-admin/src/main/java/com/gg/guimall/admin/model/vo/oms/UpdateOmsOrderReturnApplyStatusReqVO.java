package com.gg.guimall.admin.model.vo.oms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 * @description: 修改退货申请状态请求 VO
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("修改退货申请状态 VO")
public class UpdateOmsOrderReturnApplyStatusReqVO {

    @ApiModelProperty(value = "退货申请ID")
    @NotNull(message = "退货申请ID不能为空")
    private Long id;

    @ApiModelProperty(value = "目标状态：1->退货中；2->已完成；3->已拒绝")
    @NotNull(message = "目标状态不能为空")
    private Integer status;
}

