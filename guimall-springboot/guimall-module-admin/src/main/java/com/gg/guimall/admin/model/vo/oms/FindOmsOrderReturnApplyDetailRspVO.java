package com.gg.guimall.admin.model.vo.oms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 * @description: 退货申请详情响应 VO
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("退货申请详情 VO")
public class FindOmsOrderReturnApplyDetailRspVO {

    @ApiModelProperty(value = "退货申请ID")
    private Long id;

    @ApiModelProperty(value = "订单ID")
    private Long orderId;

    @ApiModelProperty(value = "订单编号")
    private String orderSn;

    @ApiModelProperty(value = "申请时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "会员账号")
    private String memberUsername;

    @ApiModelProperty(value = "退货金额")
    private BigDecimal returnAmount;

    @ApiModelProperty(value = "退货人")
    private String returnName;

    @ApiModelProperty(value = "退货电话")
    private String returnPhone;

    @ApiModelProperty(value = "状态：0->待处理；1->退货中；2->已完成；3->已拒绝")
    private Integer status;

    @ApiModelProperty(value = "商品ID")
    private Long productId;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "商品图片")
    private String productPic;

    @ApiModelProperty(value = "商品属性")
    private String productAttr;

    @ApiModelProperty(value = "商品数量")
    private Integer productCount;

    @ApiModelProperty(value = "商品单价")
    private BigDecimal productPrice;

    @ApiModelProperty(value = "退货原因")
    private String reason;

    @ApiModelProperty(value = "问题描述")
    private String description;

    @ApiModelProperty(value = "凭证图片（逗号分割）")
    private String proofPics;

    @ApiModelProperty(value = "收货地址信息（处理退货时选择）")
    private CompanyAddress companyAddress;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ApiModel("公司收货地址")
    public static class CompanyAddress {
        private Long id;
        private String addressName;
        private Integer receiveStatus;
        private String name;
        private String phone;
        private String province;
        private String city;
        private String region;
        private String detailAddress;
    }
}

