package com.gg.guimall.admin.model.vo.sms;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 * @description: 创建新品推荐请求 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "创建新品推荐请求 VO")
public class CreateSmsHomeNewProductReqVO {

    /** 商品ID */
    @NotNull(message = "商品ID不能为空")
    private Long productId;

    /** 商品名称 */
    @NotNull(message = "商品名称不能为空")
    @Size(max = 500, message = "商品名称最长500个字符")
    private String productName;

    /** 推荐状态：1推荐/0不推荐 */
    @NotNull(message = "推荐状态不能为空")
    private Integer recommendStatus;

    /** 排序 */
    @NotNull(message = "排序不能为空")
    private Integer sort;
}

