package com.gg.guimall.admin.model.vo.oms;

import com.gg.guimall.common.model.BasePageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 * @description: 退货申请分页查询请求 VO
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("退货申请分页查询 VO")
public class FindOmsOrderReturnApplyPageReqVO extends BasePageQuery {

    @ApiModelProperty(value = "订单ID")
    private Long orderId;

    @ApiModelProperty(value = "订单编号（模糊搜索）")
    private String orderSn;

    @ApiModelProperty(value = "会员账号（模糊搜索）")
    private String memberUsername;

    @ApiModelProperty(value = "状态：0->待处理；1->退货中；2->已完成；3->已拒绝")
    private Integer status;
}

