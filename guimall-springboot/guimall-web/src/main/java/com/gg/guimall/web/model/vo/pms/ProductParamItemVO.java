package com.gg.guimall.web.model.vo.pms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品参数项 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductParamItemVO {

    /** 参数定义ID */
    private Long paramId;

    /** 参数名（展示用） */
    private String key;

    /** 参数值 */
    private String value;
}
