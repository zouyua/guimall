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
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 * @description: 退货原因 DO（对应 oms_order_return_reason）
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("oms_order_return_reason")
public class OmsOrderReturnReasonDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 退货原因
     */
    private String name;

    private Integer sort;

    /**
     * 状态：0禁用 1启用
     */
    private Integer status;

    private LocalDateTime createTime;
}

