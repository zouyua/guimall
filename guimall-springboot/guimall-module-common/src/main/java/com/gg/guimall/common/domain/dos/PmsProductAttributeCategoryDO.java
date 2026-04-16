package com.gg.guimall.common.domain.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品属性分类表 DO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("pms_product_attribute_category")
public class PmsProductAttributeCategoryDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 属性分类名称
     */
    private String name;

    /**
     * 属性数量
     */
    private Integer attributeCount;

    /**
     * 参数数量
     */
    private Integer paramCount;
}
