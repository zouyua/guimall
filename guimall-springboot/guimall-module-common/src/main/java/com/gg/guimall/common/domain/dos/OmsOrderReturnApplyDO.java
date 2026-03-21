package com.gg.guimall.common.domain.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @description: 退货申请 DO（对应 oms_order_return_apply）
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("oms_order_return_apply")
public class OmsOrderReturnApplyDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long orderId;

    private Long companyAddressId;

    private Long productId;

    private String orderSn;

    private LocalDateTime createTime;

    private String memberUsername;

    private BigDecimal returnAmount;

    private String returnName;

    private String returnPhone;

    /**
     * 状态：0->待处理；1->退货中；2->已完成；3->已拒绝
     */
    private Integer status;

    private String productPic;

    private String productName;

    private String productAttr;

    private Integer productCount;

    private BigDecimal productPrice;

    private String reason;

    private String description;

    /**
     * 证明图片（逗号分割）
     */
    private String proofPics;
}

