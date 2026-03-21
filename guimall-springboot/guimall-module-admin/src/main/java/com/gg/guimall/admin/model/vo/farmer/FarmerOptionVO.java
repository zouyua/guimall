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
 * @description: 农户下拉选项VO
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "农户下拉选项 VO")
public class FarmerOptionVO {

    /**
     * 农户ID
     */
    private Long id;

    /**
     * 农户名称
     */
    private String name;
}