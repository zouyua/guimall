package com.gg.guimall.admin.model.vo.sms;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 首页轮播广告详情响应 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "首页轮播广告详情响应 VO")
public class FindSmsHomeAdvertiseDetailRspVO {

    private Long id;

    private String name;

    private Integer type;

    private String pic;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer status;

    private Integer clickCount;

    private Integer orderCount;

    private String url;

    private String note;

    private Integer sort;

    private LocalDateTime createTime;
}

