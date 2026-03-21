package com.gg.guimall.admin.model.vo.oms;

import com.gg.guimall.common.model.BasePageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 * @description: 订单列表分页查询请求 VO
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("订单列表分页查询 VO")
public class FindOmsOrderPageListReqVO extends BasePageQuery {

    @ApiModelProperty(value = "订单ID")
    private Long orderId;

    @ApiModelProperty(value = "订单编号（模糊搜索）")
    private String orderSn;

    @ApiModelProperty(value = "会员ID")
    private Long memberId;

    @ApiModelProperty(value = "会员账号（模糊搜索）")
    private String memberUsername;

    @ApiModelProperty(value = "农户ID")
    private Long farmerId;

    @ApiModelProperty(value = "订单状态：0待付款；1待发货；2已发货；3已完成；4已关闭；5无效订单")
    private Integer status;
}

