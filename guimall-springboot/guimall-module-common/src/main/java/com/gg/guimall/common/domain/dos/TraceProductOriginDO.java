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
 * 商品产地关联表 trace_product_origin
 *
 * @author wly
 * @url www.gg.com
 * @date 2026/3/19
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("trace_product_origin")
public class TraceProductOriginDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 商品ID */
    private Long productId;

    /** 产地ID */
    private Long originId;

    /** 农户ID */
    private Long farmerId;

    /** 是否主产地：0否 1是 */
    private Integer isPrimary;

    /** 创建时间 */
    private LocalDateTime createTime;
}
