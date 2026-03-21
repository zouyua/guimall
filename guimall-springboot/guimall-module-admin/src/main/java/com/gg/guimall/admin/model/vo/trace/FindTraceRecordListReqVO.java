package com.gg.guimall.admin.model.vo.trace;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author wly
 * @url www.gg.com
 * @date 2026/3/19
 * @description 查询溯源记录列表请求 VO（按商品维度）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "查询溯源记录列表请求 VO")
public class FindTraceRecordListReqVO {

    @NotNull(message = "商品ID不能为空")
    private Long productId;
}

