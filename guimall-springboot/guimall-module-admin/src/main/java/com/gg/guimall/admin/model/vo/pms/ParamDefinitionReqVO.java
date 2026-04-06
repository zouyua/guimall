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

    @ApiModelProperty("参数名")
    @NotBlank(message = "参数名不能为空")
    private String paramName;

    @ApiModelProperty("参数值")
    @NotBlank(message = "参数值不能为空")
    private String paramValue;

    @ApiModelProperty("排序")
    private Integer sort;
}
