package com.gg.guimall.web.model.vo.support;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 助农专区 - 签约农户展示 VO（前台）
 *
 * @author wly
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("助农农户展示 VO")
public class SupportFarmerRspVO {

    @ApiModelProperty(value = "农户ID")
    private Long id;

    @ApiModelProperty(value = "农户姓名")
    private String name;

    @ApiModelProperty(value = "农场/基地名称")
    private String farmName;

    @ApiModelProperty(value = "头像URL")
    private String avatar;

    @ApiModelProperty(value = "省")
    private String province;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "区/县")
    private String region;

    @ApiModelProperty(value = "主营产品")
    private String mainProduct;

    @ApiModelProperty(value = "农户/基地简介")
    private String description;

    @ApiModelProperty(value = "认证类型（逗号分隔）")
    private String certType;

    @ApiModelProperty(value = "认证描述")
    private String certDesc;
}
