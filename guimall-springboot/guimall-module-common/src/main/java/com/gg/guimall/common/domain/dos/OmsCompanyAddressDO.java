package com.gg.guimall.common.domain.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 * @description: 公司收货地址 DO（对应 oms_company_address）
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("oms_company_address")
public class OmsCompanyAddressDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String addressName;

    private Integer sendStatus;

    private Integer receiveStatus;

    private String name;

    private String phone;

    private String province;

    private String city;

    private String region;

    private String detailAddress;
}

