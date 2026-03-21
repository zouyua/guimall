package com.gg.guimall.common.domain.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 * @description: 购物车 DO（默认表名：oms_cart_item）
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("oms_cart_item")
public class OmsCartItemDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long memberId;

    private Long productId;

    private Long productSkuId;

    /** 数量 */
    private Integer quantity;

    private BigDecimal price;

    private String productPic;

    private String productName;

    private String productSubTitle;

    private String productSkuCode;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleteStatus;

    private Long productCategoryId;

    private String productSn;

    /** 销售属性(JSON) */
    private String productAttr;
}

