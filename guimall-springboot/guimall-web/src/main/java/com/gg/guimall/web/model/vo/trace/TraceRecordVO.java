package com.gg.guimall.web.model.vo.trace;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author wly
 * @url www.gg.com
 * @date 2026/3/19
 * @description 溯源记录 VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("溯源记录 VO")
public class TraceRecordVO {

    private Long recordTypeId;

    private String recordTypeName;

    private String content;

    private LocalDateTime recordTime;

    private String pic;

    /** 操作农户姓名 */
    private String farmerName;
}

