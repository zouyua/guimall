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
 * 首页轮播广告分页查询请求 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("首页轮播广告分页查询请求 VO")
public class FindSmsHomeAdvertisePageListReqVO extends BasePageQuery {

    /** 广告名称（模糊） */
    @ApiModelProperty(value = "广告名称（模糊）")
    private String name;

    /** 广告位置 */
    @ApiModelProperty(value = "广告位置：0WEB首页轮播 1APP首页轮播")
    private Integer type;

    /** 状态：0下线 1上线 */
    @ApiModelProperty(value = "状态：0下线 1上线")
    private Integer status;

    /** 开始时间（按开始时间过滤） */
    @ApiModelProperty(value = "开始时间（按开始时间过滤）")
    private LocalDateTime beginTime;

    /** 结束时间（按结束时间过滤） */
    @ApiModelProperty(value = "结束时间（按结束时间过滤）")
    private LocalDateTime endTime;
}

