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
 * @date: 2026/3/16
 * @description: 商品属性 TODO
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("pms_product_attribute")
public class PmsProductAttributeDO {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 属性分类ID
     */
    private Long productAttributeCategoryId;

    /**
     * 属性名称
     */
    private String name;

    /**
     * 选择类型
     * 0->唯一
     * 1->单选
     * 2->多选
     */
    private Integer selectType;

    /**
     * 输入类型
     * 0->手动输入
     * 1->从列表中选择
     */
    private Integer inputType;

    /**
     * 可选值列表
     */
    private String inputList;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 筛选类型
     * 0->不需要筛选
     * 1->可筛选
     */
    private Integer filterType;

    /**
     * 搜索类型
     * 0->不搜索
     * 1->关键字搜索
     * 2->范围搜索
     */
    private Integer searchType;

    /**
     * 是否关联
     * 0->不关联
     * 1->关联
     */
    private Integer relatedStatus;

    /**
     * 是否支持手动新增
     * 0->不支持
     * 1->支持
     */
    private Integer handAddStatus;

    /**
     * 属性类型
     * 0->规格
     * 1->参数
     */
    private Integer type;
}
