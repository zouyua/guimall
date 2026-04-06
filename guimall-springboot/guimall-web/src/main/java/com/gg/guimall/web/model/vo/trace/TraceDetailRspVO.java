package com.gg.guimall.web.model.vo.trace;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wly
 * @url www.gg.com
 * @date 2026/3/19
 * @description 溯源详情响应 VO（前台展示）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("溯源详情响应 VO")
public class TraceDetailRspVO {

    private Long productId;

    private String productName;

    private String productPic;

    private Long farmerId;

    private String farmerName;

    /** 农户联系电话（用于前台联系农户功能） */
    private String farmerPhone;

    private Long originId;

    private String originName;

    private String province;

    private String city;

    private String region;

    private String originDescription;

    /** 海拔高度 */
    private String altitude;

    /** 年日照时长 */
    private String sunshineHours;

    /** 土壤类型 */
    private String soilType;

    private String traceUrl;

    private String qrcodeUrl;

    private Integer scanCount;

    private List<TraceRecordVO> records;
}

