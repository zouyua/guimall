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
 * 农产品溯源记录 trace_record
 *
 * @author wly
 * @url www.gg.com
 * @date 2026/3/19
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("trace_record")
public class TraceRecordDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 商品ID */
    private Long productId;

    /** 农户ID */
    private Long farmerId;

    /** 记录类型ID（关联trace_record_type表） */
    private Long recordTypeId;

    /** 溯源内容 */
    private String content;

    /** 记录时间 */
    private LocalDateTime recordTime;

    /** 图片 */
    private String pic;

    /** 创建时间 */
    private LocalDateTime createTime;
}
