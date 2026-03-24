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
 * 会员收货地址 ums_member_receive_address
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("ums_member_receive_address")
public class UmsMemberReceiveAddressDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long memberId;
    private String name;
    private String phone;
    /** 是否默认：0 否 1 是 */
    private Integer defaultStatus;
    private String postCode;
    private String province;
    private String city;
    private String region;
    private String detailAddress;
    private LocalDateTime createTime;
}
