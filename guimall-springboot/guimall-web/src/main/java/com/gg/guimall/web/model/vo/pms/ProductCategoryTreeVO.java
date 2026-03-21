package com.gg.guimall.web.model.vo.pms;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 商品分类树结构返回 VO（前台）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("商品分类树结构返回 VO")
public class ProductCategoryTreeVO {

    private Long id;

    private String name;

    private Long parentId;

    private Integer level;

    private List<ProductCategoryTreeVO> children;
}

