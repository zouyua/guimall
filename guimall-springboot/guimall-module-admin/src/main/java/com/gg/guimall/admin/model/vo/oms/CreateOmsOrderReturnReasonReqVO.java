package com.gg.guimall.admin.model.vo.oms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 * @description: 创建退货原因请求 VO
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("创建退货原因 VO")
public class CreateOmsOrderReturnReasonReqVO {

    @ApiModelProperty(value = "退货原因")
    @NotBlank(message = "退货原因不能为空")
    private String name;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "状态：0禁用 1启用")
    private Integer status;
}

