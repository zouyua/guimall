package com.gg.guimall.common.domain.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SKU 规格明细 DO（对应 pms_sku_spec）
 *
 * 替代 pms_sku_stock.sp_data 的 JSON 存储方式，规范化存储 SKU 规格键值对。
 *
 * @author wly
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("pms_sku_spec")
public class PmsSkuSpecDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** SKU ID */
    private Long skuId;

    /** 商品ID（冗余，方便按商品查全部规格） */
    private Long productId;

    /** 规格名，如：重量、颜色、规格 */
    private String specKey;

    /** 规格值，如：3斤、红色、10个装 */
    private String specValue;

    /** 排序（升序） */
    private Integer sort;
}
