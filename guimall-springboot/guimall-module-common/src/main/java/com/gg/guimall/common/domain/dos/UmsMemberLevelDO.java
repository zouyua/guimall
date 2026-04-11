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
 * 会员等级表 ums_member_level
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("ums_member_level")
public class UmsMemberLevelDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 等级名称 */
    private String name;

    /** 等级序号（越大等级越高） */
    private Integer level;

    /** 开通/升级价格（元），0表示免费 */
    private BigDecimal price;

    /** 折扣率（百分比），如95表示打9.5折 */
    private Integer discount;

    /** 免邮门槛 */
    private BigDecimal freeFreightPoint;

    /** 评论获得成长值 */
    private Integer commentGrowthPoint;

    /** 是否有免邮特权 0否1是 */
    private Integer priviledgeFreeFreight;

    /** 是否有会员价特权 0否1是 */
    private Integer priviledgeMemberPrice;

    /** 是否有生日特权 0否1是 */
    private Integer priviledgeBirthday;

    /** 等级说明 */
    private String note;

    /** 是否为默认等级 0否1是 */
    private Integer defaultStatus;

    /** 状态 0禁用 1启用 */
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
