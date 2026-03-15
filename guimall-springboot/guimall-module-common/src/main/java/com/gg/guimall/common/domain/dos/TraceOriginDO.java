package com.gg.guimall.common.domain.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @date 2026/3/15
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
    @TableField("origin_name")
    private String originName;

    /** 省 */
    private String province;

    /** 市 */
    private String city;

    /** 区 */
    private String region;

    /** 产地介绍 */
    private String description;

    /** 创建时间 */
    private LocalDateTime createTime;
}
