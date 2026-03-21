package com.gg.guimall.web.model.vo.cart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 删除购物车商品请求 VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("删除购物车商品请求 VO")
public class DeleteCartItemReqVO {

    @ApiModelProperty(value = "会员ID")
    @NotNull(message = "memberId不能为空")
    private Long memberId;

    @ApiModelProperty(value = "购物车项ID列表")
    @NotEmpty(message = "ids不能为空")
    private List<Long> ids;
}

