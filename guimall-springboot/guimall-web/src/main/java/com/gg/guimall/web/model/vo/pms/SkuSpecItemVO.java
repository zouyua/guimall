package com.gg.guimall.web.model.vo.pms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SKU 规格项 VO（前台）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkuSpecItemVO {

    private String specKey;

    private String specValue;

    private Integer sort;
}
