package com.gg.guimall.admin.model.vo.trace;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author wly
 * @url www.gg.com
 * @date 2026/3/19
 * @description: 更新产地请求 VO（对应 trace_origin）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "更新产地请求 VO")
public class UpdateOriginReqVO {

    /** 产地ID */
    @NotNull(message = "产地ID不能为空")
    private Long id;

    /** 产地名称 */
    @NotBlank(message = "产地名称不能为空")
    @Length(max = 200, message = "产地名称最长200个字符")
    private String originName;

    /** 省 */
    @Length(max = 100)
    private String province;

    /** 市 */
    @Length(max = 100)
    private String city;

    /** 区 */
    @Length(max = 100)
    private String region;

    /** 产地介绍 */
    private String description;
}

