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
 * 商品参数定义（模板，挂在分类下）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("pms_param_definition")
public class PmsParamDefinitionDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 所属商品分类ID */
    private Long categoryId;

    /** 参数名（如：保质期、产地） */
    private String paramName;

    /** 排序 */
    private Integer sort;

    /** 创建时间 */
    private LocalDateTime createTime;
}
