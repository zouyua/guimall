package com.gg.guimall.web.model.vo.pms;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品详情响应 VO（前台）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("商品详情响应 VO")
public class FindPmsProductDetailRspVO {

    private Long id;

    private Long productCategoryId;

    private String categoryName;

    private Long farmerId;

    private String farmerName;

    private String name;

    private String subTitle;

    private String productSn;

    private String pic;

    private String albumPics;

    private String description;

    private BigDecimal price;

    private BigDecimal marketPrice;

    private BigDecimal promotionPrice;

    private Integer stock;

    private Integer lowStock;

    private String unit;

    private BigDecimal weight;

    private Integer publishStatus;

    private Integer sale;

    private String keywords;

    private String note;

    private String detailHtml;

    private Integer isNew;

    private Integer isRecommend;

    private Integer promotionType;

    private LocalDateTime promotionStartTime;

    private LocalDateTime promotionEndTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /** SKU 列表（用于详情页选择规格） */
    private List<ProductSkuStockItemVO> skus;
}

