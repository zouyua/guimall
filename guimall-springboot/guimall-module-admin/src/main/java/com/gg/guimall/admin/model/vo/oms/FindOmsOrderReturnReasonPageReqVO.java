package com.gg.guimall.admin.model.vo.oms;

import com.gg.guimall.common.model.BasePageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 * @description: 退货原因分页查询请求 VO
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("退货原因分页查询 VO")
public class FindOmsOrderReturnReasonPageReqVO extends BasePageQuery {

    @ApiModelProperty(value = "退货原因（模糊搜索）")
    private String name;

    @ApiModelProperty(value = "状态：0禁用 1启用")
    private Integer status;
}

