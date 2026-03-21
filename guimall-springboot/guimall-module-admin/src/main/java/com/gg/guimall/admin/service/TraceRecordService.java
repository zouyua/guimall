package com.gg.guimall.admin.service;

import com.gg.guimall.admin.model.vo.trace.CreateTraceRecordReqVO;
import com.gg.guimall.admin.model.vo.trace.FindTraceRecordListReqVO;
import com.gg.guimall.admin.model.vo.trace.UpdateTraceRecordReqVO;
import com.gg.guimall.common.utils.Response;

/**
 * 溯源记录 Service
 *
 * @author wly
 * @url www.gg.com
 * @date 2026/3/19
 */
public interface TraceRecordService {

    Response create(CreateTraceRecordReqVO reqVO);

    Response update(UpdateTraceRecordReqVO reqVO);

    Response delete(Long id);

    Response list(FindTraceRecordListReqVO reqVO);
}

