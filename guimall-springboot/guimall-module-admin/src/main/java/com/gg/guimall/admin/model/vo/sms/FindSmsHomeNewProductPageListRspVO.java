package com.gg.guimall.admin.model.vo.sms;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 新品推荐分页列表响应 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("新品推荐分页列表响应 VO")
public class FindSmsHomeNewProductPageListRspVO {

    @ApiModelProperty(value = "新品推荐ID")
    private Long id;

    @ApiModelProperty(value = "商品ID")
    private Long productId;

    @ApiModelProperty(value = "商品名称")
    private String productName;

    @ApiModelProperty(value = "推荐状态：1推荐/0不推荐")
    private Integer recommendStatus;

    @ApiModelProperty(value = "排序")
    private Integer sort;
}

