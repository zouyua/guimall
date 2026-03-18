package com.gg.guimall.admin.model.vo.pms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/18
 * @description: 商品满减 VO（新增/修改复用）
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "商品满减 VO")
public class PmsProductFullReductionVO {

    @ApiModelProperty(value = "满减ID（修改时必填）")
    private Long id;

    @ApiModelProperty(value = "商品ID")
    @NotNull(message = "商品ID不能为空")
    private Long productId;

    @ApiModelProperty(value = "满多少元")
    @NotNull(message = "满减金额不能为空")
    private BigDecimal fullPrice;

    @ApiModelProperty(value = "减多少元")
    @NotNull(message = "减免金额不能为空")
    private BigDecimal reducePrice;
}
