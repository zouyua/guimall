package com.gg.guimall.admin.model.vo.sms;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * 修改首页轮播广告请求 VO（对应 sms_home_advertise）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "修改首页轮播广告请求 VO")
public class UpdateSmsHomeAdvertiseReqVO {

    /** 广告ID */
    @NotNull(message = "广告ID不能为空")
    private Long id;

    /** 广告名称 */
    @NotNull(message = "广告名称不能为空")
    @Size(max = 100, message = "广告名称最长100个字符")
    private String name;

    /**
     * 广告位置：0WEB首页轮播 1APP首页轮播
     */
    @NotNull(message = "广告位置不能为空")
    private Integer type;

    /** 广告图片 */
    @NotNull(message = "广告图片不能为空")
    @Size(max = 500, message = "广告图片最长500个字符")
    private String pic;

    /** 开始时间 */
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;

    /** 结束时间 */
    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;

    /** 状态：0下线 1上线 */
    @NotNull(message = "状态不能为空")
    private Integer status;

    /** 跳转地址 */
    @Size(max = 500, message = "跳转地址最长500个字符")
    private String url;

    /** 备注 */
    @Size(max = 500, message = "备注最长500个字符")
    private String note;

    /** 排序 */
    @NotNull(message = "排序不能为空")
    private Integer sort;
}

