package com.gg.guimall.web.model.vo.cart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * 清空购物车请求 VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("清空购物车请求 VO")
public class ClearCartReqVO {

    @ApiModelProperty(value = "会员ID")
    @NotNull(message = "memberId不能为空")
    private Long memberId;
}

