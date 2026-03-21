package com.gg.guimall.admin.model.vo.farmer;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author wly
 * @url www.gg.com
 * @date 2026/3/12
 * @description: 农户详情响应 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "农户详情响应 VO")
public class FindFarmerDetailRspVO {

    /** 农户ID */
    private Long id;

    /** 农户姓名 */
    private String name;

    /** 联系电话 */
    private String phone;

    /** 身份证号 */
    private String idCard;

    /** 头像 */
    private String avatar;

    /** 农场名称 */
    private String farmName;

    /** 省 */
    private String province;

    /** 市 */
    private String city;

    /** 区 */
    private String region;

    /** 详细地址 */
    private String detailAddress;

    /** 主要农产品 */
    private String mainProduct;

    /** 农户介绍 */
    private String description;

    /** 状态：0禁用 1启用 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
