package com.gg.guimall.admin.service;

import com.gg.guimall.admin.model.vo.oms.*;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 * @description: 退货申请 Service
 **/
public interface OmsOrderReturnApplyService {

    PageResponse<FindOmsOrderReturnApplyPageRspVO> findReturnApplyPageList(FindOmsOrderReturnApplyPageReqVO reqVO);

    Response findReturnApplyDetail(Long id);

    Response updateReturnApplyStatus(UpdateOmsOrderReturnApplyStatusReqVO reqVO);
}

