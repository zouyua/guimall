package com.gg.guimall.admin.model.vo.trace;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author wly
 * @url www.gg.com
 * @date 2026/3/19
 * @description 新增/更新溯源二维码请求 VO（对应 trace_qrcode）
 * 说明：二维码图片可由前端/文件服务生成后回填 qrcodeUrl；
 * traceUrl 用于前端溯源页面路由。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "更新溯源二维码请求 VO")
public class UpsertTraceQrcodeReqVO {

    @NotNull(message = "商品ID不能为空")
    private Long productId;

    @Length(max = 500)
    private String qrcodeUrl;

    @Length(max = 500)
    private String traceUrl;
}

