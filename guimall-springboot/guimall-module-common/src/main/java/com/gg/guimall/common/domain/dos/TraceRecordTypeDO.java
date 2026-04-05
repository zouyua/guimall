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
 * 溯源记录类型 trace_record_type
 *
 * @author wly
 * @url www.gg.com
 * @date 2026/04/03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("trace_record_type")
public class TraceRecordTypeDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 商品分类ID */
    private Long categoryId;

    /** 记录类型名称 */
    private String typeName;

    /** 排序 */
    private Integer sort;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
