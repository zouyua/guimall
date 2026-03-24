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
 * @date 2026/3/24
 * @description 产地绑定商品列表响应 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "产地绑定商品列表响应 VO")
public class FindOriginBindingRspVO {

    private Long relationId;

    private Long productId;

    private String productName;

    private Long originId;

    private String originName;

    private Long farmerId;

    private String farmerName;

    private LocalDateTime createTime;
}

