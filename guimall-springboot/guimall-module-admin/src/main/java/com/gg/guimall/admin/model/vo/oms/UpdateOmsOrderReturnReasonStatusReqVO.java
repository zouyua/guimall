package com.gg.guimall.admin.model.vo.oms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 * @description: 修改退货原因状态请求 VO
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("修改退货原因状态 VO")
public class UpdateOmsOrderReturnReasonStatusReqVO {

    @ApiModelProperty(value = "ID")
    @NotNull(message = "ID不能为空")
    private Long id;

    @ApiModelProperty(value = "状态：0禁用 1启用")
    @NotNull(message = "状态不能为空")
    private Integer status;
}

