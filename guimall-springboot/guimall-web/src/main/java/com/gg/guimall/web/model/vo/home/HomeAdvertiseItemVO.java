package com.gg.guimall.web.model.vo.home;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 首页轮播广告项 VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("首页轮播广告项 VO")
public class HomeAdvertiseItemVO {

    private Long id;

    /** 广告名称 */
    private String name;

    /** 广告图片 */
    private String pic;

    /** 跳转地址 */
    private String url;

    /** 排序 */
    private Integer sort;
}

