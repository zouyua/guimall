package com.gg.guimall.common.domain.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/13
 * @description: 商品属性分类表
 **/
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
     * 属性数量（规格数量）
     */
    private Integer attributeCount;

    /**
     * 参数数量
     */
    private Integer paramCount;
}
