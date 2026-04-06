package com.gg.guimall.admin.model.vo.pms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品参数项 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "商品参数项 VO")
public class ProductParamItemVO {

    @ApiModelProperty(value = "参数定义ID")
    private Long paramId;

    @ApiModelProperty(value = "参数名（展示用，后端填充）")
    private String key;

    @ApiModelProperty(value = "参数值")
    private String value;
}
