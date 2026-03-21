package com.gg.guimall.web.model.vo.cart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * 添加购物车请求 VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("添加购物车请求 VO")
public class AddCartItemReqVO {

    @ApiModelProperty(value = "会员ID")
    @NotNull(message = "memberId不能为空")
    private Long memberId;

    @ApiModelProperty(value = "商品ID")
    @NotNull(message = "productId不能为空")
    private Long productId;

    @ApiModelProperty(value = "SKU ID")
    @NotNull(message = "productSkuId不能为空")
    private Long productSkuId;

    @ApiModelProperty(value = "数量")
    @NotNull(message = "quantity不能为空")
    private Integer quantity;

    @ApiModelProperty(value = "销售属性(JSON)，用于区分规格")
    private String productAttr;
}

