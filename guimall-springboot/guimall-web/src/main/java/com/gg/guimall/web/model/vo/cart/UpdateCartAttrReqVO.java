package com.gg.guimall.web.model.vo.cart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * 修改购物车商品规格/属性请求 VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("修改购物车商品规格请求 VO")
public class UpdateCartAttrReqVO {

    @ApiModelProperty(value = "会员ID")
    @NotNull(message = "memberId不能为空")
    private Long memberId;

    @ApiModelProperty(value = "购物车项ID")
    @NotNull(message = "id不能为空")
    private Long id;

    @ApiModelProperty(value = "商品ID")
    @NotNull(message = "productId不能为空")
    private Long productId;

    @ApiModelProperty(value = "SKU ID")
    @NotNull(message = "productSkuId不能为空")
    private Long productSkuId;

    @ApiModelProperty(value = "销售属性(JSON)")
    private String productAttr;
}

