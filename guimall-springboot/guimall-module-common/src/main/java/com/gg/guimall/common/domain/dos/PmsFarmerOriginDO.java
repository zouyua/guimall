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
 * 农户-产地关联表 pms_farmer_origin (M:N)
 * 一个农户可以在多个产地种植，一个产地可以有多个农户
 *
 * @author wly
 * @date 2026/4/4
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("pms_farmer_origin")
public class PmsFarmerOriginDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 农户ID */
    private Long farmerId;

    /** 产地ID */
    private Long originId;

    /** 创建时间 */
    private LocalDateTime createTime;
}
