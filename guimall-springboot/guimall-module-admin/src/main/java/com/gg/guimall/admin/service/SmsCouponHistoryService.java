package com.gg.guimall.admin.service;

import com.gg.guimall.admin.model.vo.sms.FindSmsCouponHistoryPageListReqVO;
import com.gg.guimall.admin.model.vo.sms.FindSmsCouponHistoryPageListRspVO;
import com.gg.guimall.common.utils.PageResponse;

/**
 * 优惠券领取记录管理 Service
 *
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 */
public interface SmsCouponHistoryService {

    PageResponse<FindSmsCouponHistoryPageListRspVO> findCouponHistoryPageList(FindSmsCouponHistoryPageListReqVO reqVO);
}

