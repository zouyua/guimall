package com.gg.guimall.admin.service;

import com.gg.guimall.admin.model.vo.trace.CreateOriginReqVO;
import com.gg.guimall.admin.model.vo.trace.FindOriginPageListReqVO;
import com.gg.guimall.admin.model.vo.trace.UpdateOriginReqVO;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;

/**
 * 产地管理 Service
 *
 * @author wly
 * @url www.gg.com
 * @date 2026/3/19
 */
public interface TraceOriginService {

    Response createOrigin(CreateOriginReqVO createOriginReqVO);

    PageResponse findOriginPageList(FindOriginPageListReqVO findOriginPageListReqVO);

    Response findOriginDetail(Long id);

    Response updateOrigin(UpdateOriginReqVO updateOriginReqVO);

    Response deleteOrigin(Long id);

    Response findOriginOptions();
}

