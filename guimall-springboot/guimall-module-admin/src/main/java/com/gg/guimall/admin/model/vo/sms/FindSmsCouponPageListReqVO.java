package com.gg.guimall.admin.model.vo.sms;

import com.gg.guimall.common.model.BasePageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 * @description: 优惠券分页查询请求 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("优惠券分页查询请求")
public class FindSmsCouponPageListReqVO extends BasePageQuery {

    /** 优惠券名称（模糊） */
    @ApiModelProperty(value = "优惠券名称（模糊）")
    private String name;

    /** 优惠券类型 */
    @ApiModelProperty(value = "优惠券类型")
    private Integer type;

    /** 使用范围 */
    @ApiModelProperty(value = "使用范围：0全场1指定分类2指定商品")
    private Integer useType;

    /** 使用平台 */
    @ApiModelProperty(value = "使用平台：0全部1移动端2WEB")
    private Integer platform;

    /** 开始时间（按优惠券有效期 startTime 过滤） */
    @ApiModelProperty(value = "开始时间（按优惠券有效期 startTime）")
    private LocalDateTime beginTime;

    /** 结束时间（按优惠券有效期 endTime 过滤） */
    @ApiModelProperty(value = "结束时间（按优惠券有效期 endTime）")
    private LocalDateTime endTime;
}

