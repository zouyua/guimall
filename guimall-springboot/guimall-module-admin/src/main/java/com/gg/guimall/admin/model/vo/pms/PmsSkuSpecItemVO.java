package com.gg.guimall.admin.model.vo.pms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SKU 规格项 VO（规格名+规格值）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("SKU规格项")
public class PmsSkuSpecItemVO {

    @ApiModelProperty(value = "规格名，如：重量、颜色")
    private String specKey;

    @ApiModelProperty(value = "规格值，如：3斤、红色")
    private String specValue;

    @ApiModelProperty(value = "排序")
    private Integer sort;
}
