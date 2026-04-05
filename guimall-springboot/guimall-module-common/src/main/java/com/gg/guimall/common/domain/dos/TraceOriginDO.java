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
 * 农产品产地表 trace_origin
 *
 * @author wly
 * @url www.gg.com
 * @date 2026/3/19
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("trace_origin")
public class TraceOriginDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 产地名称 */
    private String originName;

    /** 省 */
    private String province;

    /** 市 */
    private String city;

    /** 区 */
    private String region;

    /** 产地介绍 */
    private String description;

    /** 经度（GIS定位） */
    private java.math.BigDecimal longitude;

    /** 纬度（GIS定位） */
    private java.math.BigDecimal latitude;

    /** 海拔高度（如 240m-450m） */
    private String altitude;

    /** 年日照时长（如 1650h/年） */
    private String sunshineHours;

    /** 土壤类型（如 富硒红壤） */
    private String soilType;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
