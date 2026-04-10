package com.gg.guimall.web.service;

import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;
import com.gg.guimall.web.model.vo.oms.FindOmsOrderDetailReqVO;
import com.gg.guimall.web.model.vo.oms.FindOmsOrderDetailRspVO;
import com.gg.guimall.web.model.vo.oms.FindOmsOrderPageListReqVO;
import com.gg.guimall.web.model.vo.oms.FindOmsOrderPageListRspVO;
import com.gg.guimall.web.model.vo.oms.SubmitOmsOrderReqVO;
import com.gg.guimall.web.model.vo.oms.SubmitOmsOrderRspVO;

/**
 * 前台订单 Service
 */
public interface OmsOrderService {

    Response submitOrder(SubmitOmsOrderReqVO reqVO);

    PageResponse<FindOmsOrderPageListRspVO> findOrderPageList(FindOmsOrderPageListReqVO reqVO);

    Response findOrderDetail(FindOmsOrderDetailReqVO reqVO);

    /**
     * 前台用户确认收货
     */
    Response confirmReceipt(Long orderId, Long memberId);
}

