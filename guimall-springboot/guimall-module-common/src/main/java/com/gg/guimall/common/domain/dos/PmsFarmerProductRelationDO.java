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
 * 农户商品关联表 pms_farmer_product_relation
 * 一个农户可以卖多个商品
 *
 * @author wly
 * @url www.gg.com
 * @date 2026/3/15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("pms_farmer_product_relation")
public class PmsFarmerProductRelationDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 农户ID */
    private Long farmerId;

    /** 商品ID */
    private Long productId;

    /** 产地（该关联下的产地说明） */
    private String originPlace;

    /** 创建时间 */
    private LocalDateTime createTime;
}
