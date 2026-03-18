package com.gg.guimall.admin.model.vo.pms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/13
 * @description: 创建商品属性分类请求 VO
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "创建商品属性分类请求 VO")
public class PmsProductAttributeCategoryCreateReqVO {

    @ApiModelProperty(value = "属性分类名称")
    @NotBlank(message = "属性分类名称不能为空")
    private String name;
}
