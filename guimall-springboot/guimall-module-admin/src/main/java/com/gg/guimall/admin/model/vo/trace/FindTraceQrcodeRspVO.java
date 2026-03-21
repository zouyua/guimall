package com.gg.guimall.admin.model.vo.trace;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author wly
 * @url www.gg.com
 * @date 2026/3/19
 * @description 溯源二维码响应 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "溯源二维码响应 VO")
public class FindTraceQrcodeRspVO {

    private Long id;

    private Long productId;

    private String qrcodeUrl;

    private String traceUrl;

    private Integer scanCount;

    private LocalDateTime createTime;
}

