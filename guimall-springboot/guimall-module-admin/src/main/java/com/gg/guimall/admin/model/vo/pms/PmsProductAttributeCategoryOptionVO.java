package com.gg.guimall.admin.model.vo.pms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品属性分类下拉选项 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("商品属性分类下拉选项 VO")
public class PmsProductAttributeCategoryOptionVO {

    @ApiModelProperty("属性分类ID")
    private Long id;

    @ApiModelProperty("属性分类名称")
    private String name;
}
