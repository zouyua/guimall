package com.gg.guimall.common.domain.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 首页轮播广告 DO（对应 sms_home_advertise）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sms_home_advertise")
public class SmsHomeAdvertiseDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 广告名称 */
    private String name;

    /**
     * 广告位置：0WEB首页轮播 1APP首页轮播
     */
    private Integer type;

    /** 广告图片 */
    private String pic;

    /** 开始时间 */
    private LocalDateTime startTime;

    /** 结束时间 */
    private LocalDateTime endTime;

    /** 状态：0下线 1上线 */
    private Integer status;

    /** 点击量 */
    private Integer clickCount;

    /** 下单量 */
    private Integer orderCount;

    /** 跳转地址 */
    private String url;

    /** 备注 */
    private String note;

    /** 排序 */
    private Integer sort;

    /** 创建时间 */
    private LocalDateTime createTime;
}

