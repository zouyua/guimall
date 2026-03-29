package com.gg.guimall.common.domain.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/12
 * @description: 商品分类表
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("pms_product_category")
public class PmsProductCategoryDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 父分类ID：0表示一级分类
     */
    private Long parentId;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类级别：0->1级；1->2级
     */
    private Integer level;

    /**
     * 单位
     */
    private String productUnit;

    /**
     * 启用状态：0禁用 1启用（合并原 navStatus + showStatus）
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 分类图标
     */
    private String icon;

    /**
     * 关键词
     */
    private String keywords;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}