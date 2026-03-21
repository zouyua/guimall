package com.gg.guimall.common.domain.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 人气推荐商品表（首页显示）DO
 *
 * 对应表：sms_home_recommend_product
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sms_home_recommend_product")
public class SmsHomeRecommendProductDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 商品ID */
    private Long productId;

    /** 商品名称 */
    private String productName;

    /** 推荐状态：1推荐/0不推荐 */
    private Integer recommendStatus;

    /** 排序 */
    private Integer sort;
}

