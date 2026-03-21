package com.gg.guimall.admin.model.vo.trace;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wly
 * @url www.gg.com
 * @date 2026/3/19
 * @description 分页查询产地请求
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "分页查询产地请求 VO")
public class FindOriginPageListReqVO {

    /** 当前页 */
    private Long current = 1L;

    /** 每页大小 */
    private Long size = 10L;

    /** 产地名称（模糊） */
    private String originName;

    /** 省（模糊） */
    private String province;

    /** 市（模糊） */
    private String city;

    /** 区（模糊） */
    private String region;
}

