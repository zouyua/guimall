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
 * @description 商品产地关联响应 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "商品产地关联响应 VO")
public class FindProductOriginRspVO {

    private Long id;

    private Long productId;

    private Long originId;

    private Long farmerId;

    private String originName;

    private String farmerName;

    private LocalDateTime createTime;
}

