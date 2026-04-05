package com.gg.guimall.admin.model.vo.trace;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 *
 * @author wly
 * @url www.gg.com
 * @date 2026/3/19
 * @description 创建溯源记录请求
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "创建溯源记录请求 VO")
public class CreateTraceRecordReqVO {

    @NotNull(message = "商品ID不能为空")
    private Long productId;

    private Long farmerId;

    @NotNull(message = "记录类型不能为空")
    private Long recordTypeId;

    private String content;

    private LocalDateTime recordTime;

    @Length(max = 500)
    private String pic;
}

