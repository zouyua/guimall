package com.gg.guimall.web.model.vo.pms;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品列表项 VO（前台）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("商品列表项 VO")
public class ProductPageItemVO {

    private Long id;

    private Long productCategoryId;

    private String categoryName;

    private Long farmerId;

    private String farmerName;

    private String name;

    private String subTitle;

    private String pic;

    private BigDecimal price;

    private Integer stock;

    private Integer publishStatus;

    private Integer sale;

    private LocalDateTime createTime;
}

