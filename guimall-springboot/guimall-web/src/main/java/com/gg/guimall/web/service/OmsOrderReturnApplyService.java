package com.gg.guimall.web.service;

import com.gg.guimall.common.utils.Response;
import com.gg.guimall.web.model.vo.oms.CancelOmsOrderReqVO;
import com.gg.guimall.web.model.vo.oms.CreateOmsOrderReturnApplyReqVO;

import java.util.List;

/**
 * 前台订单退货 Service
 */
public interface OmsOrderReturnApplyService {

    /**
     * 创建退货申请
     */
    Response createReturnApply(CreateOmsOrderReturnApplyReqVO reqVO);

    /**
     * 取消订单
     */
    Response cancelOrder(CancelOmsOrderReqVO reqVO);

    /**
     * 查询启用的退货原因列表
     */
    Response findReturnReasonList();
}
