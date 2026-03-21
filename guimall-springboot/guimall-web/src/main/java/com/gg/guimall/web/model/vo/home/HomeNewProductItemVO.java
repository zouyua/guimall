package com.gg.guimall.web.model.vo.home;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 首页新品推荐商品项 VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("首页新品推荐商品项 VO")
public class HomeNewProductItemVO {

    /** 首页推荐记录ID */
    private Long id;

    private Long productId;

    private String productName;

    private String productPic;

    private BigDecimal price;

    private Integer sort;
}

