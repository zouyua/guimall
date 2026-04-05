package com.gg.guimall.admin.model.vo.farmer;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * @author wly
 * @url www.gg.com
 * @date 2026/3/12
 * @description: 更新农户请求 VO（对应 pms_farmer 表）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "更新农户请求 VO")
public class UpdateFarmerReqVO {

    /** 农户ID */
    @NotNull(message = "农户ID不能为空")
    private Long id;

    /** 农户姓名 */
    @NotBlank(message = "农户姓名不能为空")
    @Length(max = 100, message = "农户姓名最长100个字符")
    private String name;

    /** 联系电话 */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "联系电话格式不正确")
    @Length(max = 64)
    private String phone;

    /** 身份证号 */
    @Length(max = 64)
    private String idCard;

    /** 头像 */
    @Length(max = 500)
    private String avatar;

    /** 农场名称 */
    @Length(max = 200)
    private String farmName;

    /** 省 */
    @Length(max = 100)
    private String province;

    /** 市 */
    @Length(max = 100)
    private String city;

    /** 区 */
    @Length(max = 100)
    private String region;

    /** 详细地址 */
    @Length(max = 255)
    private String detailAddress;

    /** 主要农产品 */
    @Length(max = 200)
    private String mainProduct;

    /** 农户介绍 */
    private String description;

    /** 状态：0禁用 1启用 */
    private Integer status;

    /** 关联产地ID列表 */
    private List<Long> originIds;
}
