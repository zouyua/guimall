package com.gg.guimall.admin.model.vo.trace;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author wly
 * @url www.gg.com
 * @date 2026/3/19
 * @description 更新溯源记录请求 VO（对应 trace_record）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "更新溯源记录请求 VO")
public class UpdateTraceRecordReqVO {

    @NotNull(message = "记录ID不能为空")
    private Long id;

    @NotNull(message = "商品ID不能为空")
    private Long productId;

    private Long farmerId;

    @NotBlank(message = "记录类型不能为空")
    @Length(max = 100, message = "记录类型最长100个字符")
    private String recordType;

    private String content;

    private LocalDateTime recordTime;

    @Length(max = 500)
    private String pic;
}

