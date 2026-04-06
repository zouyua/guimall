package com.gg.guimall.web.model.vo.oms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 创建退货申请请求 VO（前台）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("创建退货申请请求 VO")
public class CreateOmsOrderReturnApplyReqVO {

    @NotNull(message = "订单ID不能为空")
    @ApiModelProperty(value = "订单ID", required = true)
    private Long orderId;

    @NotNull(message = "会员ID不能为空")
    @ApiModelProperty(value = "会员ID", required = true)
    private Long memberId;

    @ApiModelProperty(value = "商品ID")
    private Long productId;

    @NotNull(message = "退货原因不能为空")
    @ApiModelProperty(value = "退货原因", required = true)
    private String reason;

    @ApiModelProperty(value = "问题描述")
    private String description;

    @ApiModelProperty(value = "证明图片（逗号分割）")
    private String proofPics;

    @ApiModelProperty(value = "退货人姓名")
    private String returnName;

    @ApiModelProperty(value = "退货人电话")
    private String returnPhone;

    @NotNull(message = "退货金额不能为空")
    @ApiModelProperty(value = "退货金额", required = true)
    private BigDecimal returnAmount;
}
