package com.gg.guimall.admin.model.vo.sms;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 新品推荐下拉选项 VO（仅推荐状态）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "新品推荐下拉选项 VO")
public class HomeNewProductOptionVO {

    private Long id;

    private Long productId;

    private String productName;

    private Integer sort;
}

