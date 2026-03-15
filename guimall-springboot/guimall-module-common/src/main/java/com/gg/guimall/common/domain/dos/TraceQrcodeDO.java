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
 * 溯源二维码表 trace_qrcode
 *
 * @author wly
 * @url www.gg.com
 * @date 2026/3/15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("trace_qrcode")
public class TraceQrcodeDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 商品ID */
    private Long productId;

    /** 二维码图片 */
    private String qrcodeUrl;

    /** 溯源页面地址 */
    private String traceUrl;

    /** 扫码次数 */
    private Integer scanCount;

    /** 创建时间 */
    private LocalDateTime createTime;
}
