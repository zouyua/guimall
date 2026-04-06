package com.gg.guimall.web.model.vo.oms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 退货原因列表响应 VO（前台）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("退货原因列表响应 VO")
public class FindOmsOrderReturnReasonListRspVO {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "退货原因")
    private String name;

    @ApiModelProperty(value = "排序")
    private Integer sort;
}
