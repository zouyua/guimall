package com.gg.guimall.admin.model.vo.sms;

import com.gg.guimall.common.model.BasePageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 新品推荐分页查询请求 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("新品推荐分页查询请求 VO")
public class FindSmsHomeNewProductPageListReqVO extends BasePageQuery {

    /** 商品名称（模糊） */
    @ApiModelProperty(value = "商品名称（模糊）")
    private String productName;

    /** 推荐状态：1推荐/0不推荐 */
    @ApiModelProperty(value = "推荐状态：1推荐/0不推荐")
    private Integer recommendStatus;
}

