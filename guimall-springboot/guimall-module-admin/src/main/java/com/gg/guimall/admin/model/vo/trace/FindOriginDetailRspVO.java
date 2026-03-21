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
 * @description 产地详情响应 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "产地详情响应 VO")
public class FindOriginDetailRspVO {

    private Long id;

    private String originName;

    private String province;

    private String city;

    private String region;

    private String description;

    private LocalDateTime createTime;
}

