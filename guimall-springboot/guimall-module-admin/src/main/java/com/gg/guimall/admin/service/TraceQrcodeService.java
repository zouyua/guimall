package com.gg.guimall.admin.service;

import com.gg.guimall.admin.model.vo.trace.UpsertTraceQrcodeReqVO;
import com.gg.guimall.common.utils.Response;

/**
 * 溯源二维码 Service
 *
 * @author wly
 * @url www.gg.com
 * @date 2026/3/19
 */
public interface TraceQrcodeService {

    Response upsert(UpsertTraceQrcodeReqVO reqVO);

    Response findByProductId(Long productId);

    Response deleteByProductId(Long productId);
}

