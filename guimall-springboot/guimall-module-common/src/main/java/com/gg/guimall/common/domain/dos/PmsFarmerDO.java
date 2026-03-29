package com.gg.guimall.common.domain.dos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 农户表 pms_farmer
 *
 * @author wly
 * @url www.gg.com
 * @date 2026/3/15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("pms_farmer")
public class PmsFarmerDO {

    @TableId(type = IdType.AUTO)
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

    /** 是否删除：0否 1是 */
    @com.baomidou.mybatisplus.annotation.TableLogic(value = "0", delval = "1")
    private Integer isDeleted;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
