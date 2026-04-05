package com.gg.guimall.admin.model.vo.pms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("参数定义响应 VO")
public class ParamDefinitionRspVO {

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("参数名")
    private String paramName;

    @ApiModelProperty("参数值")
    private String paramValue;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
