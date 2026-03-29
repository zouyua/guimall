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
 * @description: 订单 DO（对应 oms_order）
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("oms_order")
public class OmsOrderDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long memberId;

    private Long farmerId;

    private Long couponId;

    private String orderSn;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String memberUsername;

    private BigDecimal totalAmount;

    private BigDecimal payAmount;

    private BigDecimal freightAmount;

    private BigDecimal promotionAmount;

    private BigDecimal integrationAmount;

    private BigDecimal couponAmount;

    private BigDecimal discountAmount;

    /**
     * 支付方式:0未支付1支付宝2微信
     */
    private Integer payType;

    /**
     * 订单来源:0WEB1APP
     */
    private Integer sourceType;

    /**
     * 订单状态:0待付款1待发货2已发货3已完成4已关闭5无效订单
     */
    private Integer status;

    private String deliveryCompany;

    private String deliverySn;

    private String receiverName;

    private String receiverPhone;

    private String receiverPostCode;

    private String receiverProvince;

    private String receiverCity;

    private String receiverRegion;

    private String receiverDetailAddress;

    private String note;

    private String adminNote;

    private Integer confirmStatus;

    @TableLogic(value = "0", delval = "1")
    private Integer isDeleted;

    private LocalDateTime paymentTime;

    private String paymentSn;

    private LocalDateTime deliveryTime;

    private LocalDateTime receiveTime;
}

