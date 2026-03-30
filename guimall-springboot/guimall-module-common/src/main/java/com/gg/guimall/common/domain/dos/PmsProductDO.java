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
 * @date: 2026/3/10
 * @description:
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("pms_product")
public class PmsProductDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商品分类ID
     */
    private Long productCategoryId;

    /**
     * 商品属性分类ID
     */
    private Long productAttributeCategoryId;

    /**
     * 农户ID
     */
    private Long farmerId;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 副标题
     */
    private String subTitle;

    /**
     * 商品货号
     */
    private String productSn;

    /**
     * 主图
     */
    private String pic;

    /**
     * 商品相册图片（逗号分割）
     */
    private String albumPics;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 销售价格
     */
    private BigDecimal price;

    /**
     * 原价/市场价（用于显示划线价）
     */
    private BigDecimal marketPrice;

    /**
     * 促销价格
     */
    private BigDecimal promotionPrice;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 库存预警值
     */
    private Integer lowStock;

    /**
     * 单位（斤/箱/袋）
     */
    private String unit;

    /**
     * 重量（克）
     */
    private BigDecimal weight;

    /**
     * 上架状态：0下架，1上架
     */
    private Integer publishStatus;

    /**
     * 是否新品推荐：0否 1是
     */
    private Integer isNew;

    /**
     * 是否人气推荐：0否 1是
     */
    private Integer isRecommend;

    /**
     * 删除状态：0未删除，1已删除
     */
    @TableLogic(value = "0", delval = "1")
    private Integer isDeleted;

    /**
     * 销量
     */
    private Integer sale;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 关键词
     */
    private String keywords;

    /**
     * 备注
     */
    private String note;

    /**
     * 产品详情网页内容
     */
    private String detailHtml;

    /**
     * 是否助农商品：0否 1是（用于助农专区展示）
     */
    private Integer isAidAgriculture;

    /**
     * 认证类型：如 绿色食品、有机认证、地理标志等（逗号分隔）
     */
    private String certType;

    /**
     * 认证描述
     */
    private String certDesc;

    /**
     * 促销类型：0没有促销使用原价；1使用满减价格；2使用会员价
     */
    private Integer promotionType;

    /**
     * 促销开始时间
     */
    private LocalDateTime promotionStartTime;

    /**
     * 促销结束时间
     */
    private LocalDateTime promotionEndTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}