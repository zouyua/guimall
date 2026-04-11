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
 * @date: 2026/4/11
 * @description: 积分变动历史 DO（对应 ums_integration_history）
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("ums_integration_history")
public class UmsIntegrationHistoryDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 变动积分数量（正数）
     */
    private Integer changeCount;

    /**
     * 变动类型：0获取 1消费
     */
    private Integer changeType;

    /**
     * 积分来源：0订单完成奖励 1下单积分抵扣 2管理员手动调整 3注册赠送 4订单取消退还
     */
    private Integer sourceType;

    /**
     * 来源业务ID（如订单ID）
     */
    private Long sourceId;

    /**
     * 备注说明
     */
    private String note;

    private LocalDateTime createTime;
}
