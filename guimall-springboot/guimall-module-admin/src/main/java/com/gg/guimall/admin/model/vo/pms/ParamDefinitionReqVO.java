package com.gg.guimall.admin.model.vo.pms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("创建/修改参数定义请求 VO")
public class ParamDefinitionReqVO {

    @ApiModelProperty("参数定义ID（修改时必填）")
    private Long id;

    @ApiModelProperty("所属分类ID")
    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

    @ApiModelProperty("参数名")
    @NotBlank(message = "参数名不能为空")
    private String paramName;

    @ApiModelProperty("排序")
    private Integer sort;
}
