package com.gg.guimall.admin.service;

import com.gg.guimall.admin.model.vo.oms.*;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;

import java.util.List;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 * @description: 退货原因 Service
 **/
public interface OmsOrderReturnReasonService {

    PageResponse<FindOmsOrderReturnReasonPageRspVO> findReturnReasonPageList(FindOmsOrderReturnReasonPageReqVO reqVO);

    Response findReturnReasonDetail(Long id);

    Response createReturnReason(CreateOmsOrderReturnReasonReqVO reqVO);

    Response updateReturnReason(UpdateOmsOrderReturnReasonReqVO reqVO);

    Response updateReturnReasonStatus(UpdateOmsOrderReturnReasonStatusReqVO reqVO);

    Response deleteReturnReasons(List<Long> ids);
}

