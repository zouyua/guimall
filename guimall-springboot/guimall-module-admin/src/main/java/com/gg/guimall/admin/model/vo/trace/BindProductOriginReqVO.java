package com.gg.guimall.admin.model.vo.trace;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 *
 * @author wly
 * @url www.gg.com
 * @date 2026/3/19
 * @description: 绑定商品产地请求 VO（对应 trace_product_origin）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "绑定商品产地请求 VO")
public class BindProductOriginReqVO {

    /** 商品ID */
    @NotNull(message = "商品ID不能为空")
    private Long productId;

    /** 产地ID */
    @NotNull(message = "产地ID不能为空")
    private Long originId;

    /** 农户ID（可选：不传则使用商品自身 farmerId） */
    private Long farmerId;
}

