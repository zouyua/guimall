package com.gg.guimall.common.domain.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品参数表（自由键值对）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("pms_product_param")
public class PmsProductParamDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 商品ID */
    private Long productId;

    /** 参数定义ID */
    private Long paramId;

    /** 参数值 */
    private String paramValue;

    /** 排序 */
    private Integer sort;
}
