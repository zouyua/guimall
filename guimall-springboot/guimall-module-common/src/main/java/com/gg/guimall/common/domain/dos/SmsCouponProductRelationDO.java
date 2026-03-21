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
 * @date: 2026/3/19
 * @description: 优惠券商品关系 DO（对应 sms_coupon_product_relation）
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sms_coupon_product_relation")
public class SmsCouponProductRelationDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long couponId;

    private Long productId;

    private String productName;

    private String productSn;
}

