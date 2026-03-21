package com.gg.guimall.admin.model.vo.trace;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wly
 * @url www.gg.com
 * @date 2026/3/19
 * @description 产地下拉选项 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "产地下拉选项 VO")
public class OriginOptionVO {

    private Long id;

    private String originName;
}

