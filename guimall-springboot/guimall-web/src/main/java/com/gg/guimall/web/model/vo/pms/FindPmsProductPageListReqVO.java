package com.gg.guimall.web.model.vo.pms;

import com.gg.guimall.common.model.BasePageQuery;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

/**
 * 商品分页查询请求 VO（前台）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("商品分页查询请求 VO")
public class FindPmsProductPageListReqVO extends BasePageQuery {

    /** 关键字（商品名模糊） */
    @Size(max = 200, message = "关键字最长200个字符")
    private String keyword;

    /** 分类ID */
    private Long categoryId;

    /**
     * 排序类型
     * 0/NULL: 创建时间倒序
     * 1: 销量倒序
     * 2: 销售价格倒序
     */
    private Integer sortType;
}

