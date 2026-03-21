package com.gg.guimall.admin.model.vo.farmer;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/12
 * @description: 分页查询农户请求VO
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "分页查询农户请求 VO")
public class FindFarmerPageListReqVO {

    /**
     * 当前页
     */
    private Long current = 1L;

    /**
     * 每页大小
     */
    private Long size = 10L;

    /**
     * 农户名称（模糊搜索）
     */
    private String name;

    /**
     * 联系电话（模糊搜索）
     */
    private String phone;

    /**
     * 状态：0->禁用；1->启用
     */
    private Integer status;
}