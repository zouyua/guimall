package com.gg.guimall.admin.model.vo.sms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 首页轮播广告分页列表响应 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("首页轮播广告分页列表响应 VO")
public class FindSmsHomeAdvertisePageListRspVO {

    @ApiModelProperty(value = "广告ID")
    private Long id;

    @ApiModelProperty(value = "广告名称")
    private String name;

    @ApiModelProperty(value = "广告位置：0WEB首页轮播 1APP首页轮播")
    private Integer type;

    @ApiModelProperty(value = "广告图片")
    private String pic;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "状态：0下线 1上线")
    private Integer status;

    @ApiModelProperty(value = "点击量")
    private Integer clickCount;

    @ApiModelProperty(value = "下单量")
    private Integer orderCount;

    @ApiModelProperty(value = "跳转地址")
    private String url;

    @ApiModelProperty(value = "备注")
    private String note;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}

