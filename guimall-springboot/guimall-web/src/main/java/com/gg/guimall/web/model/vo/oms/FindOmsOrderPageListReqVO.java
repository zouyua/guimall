package com.gg.guimall.web.model.vo.oms;

import com.gg.guimall.common.model.BasePageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单列表分页查询请求 VO（前台）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("订单列表分页查询请求 VO")
public class FindOmsOrderPageListReqVO extends BasePageQuery {

    @ApiModelProperty(value = "会员ID")
    private Long memberId;

    @ApiModelProperty(value = "订单编号（模糊搜索）")
    private String orderSn;

    /**
     * 订单状态：0待付款；1待发货；2已发货；3已完成；4已关闭；5无效订单
     */
    @ApiModelProperty(value = "订单状态")
    private Integer status;
}

