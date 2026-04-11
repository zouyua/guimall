package com.gg.guimall.admin.service;

import com.gg.guimall.admin.model.vo.oms.FindOmsOrderPageListReqVO;
import com.gg.guimall.admin.model.vo.oms.FindOmsOrderPageListRspVO;
import com.gg.guimall.admin.model.vo.oms.*;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 * @description: 订单管理 Service
 **/
public interface OmsOrderService {

    /**
     * 订单列表分页查询
     */
    PageResponse<FindOmsOrderPageListRspVO> findOrderPageList(FindOmsOrderPageListReqVO reqVO);

    /**
     * 订单详情
     */
    Response findOrderDetail(Long id);

    /**
     * 发货
     */
    Response deliverOrder(DeliverOmsOrderReqVO reqVO);

    /**
     * 关闭订单
     */
    Response closeOrder(CloseOmsOrderReqVO reqVO);

    /**
     * 备注订单
     */
    Response remarkOrder(RemarkOmsOrderReqVO reqVO);

    /**
     * 确认收货
     */
    Response confirmReceipt(Long id);
}

