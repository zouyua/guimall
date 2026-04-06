package com.gg.guimall.admin.model.vo.farmer;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wly
 * @url www.gg.com
 * @date 2026/4/4
 * @description: 农户列表响应 VO（包含关联产地名称）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "农户列表响应 VO")
public class FindFarmerPageListRspVO {

    private Long id;
    private String name;
    private String phone;
    private String idCard;
    private String avatar;
    private String farmName;
    private String province;
    private String city;
    private String region;
    private String detailAddress;
    private String mainProduct;
    private String description;
    private String certType;
    private String certDesc;
    private String certPic;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    /** 关联产地名称列表 */
    private List<String> originNames;
}
