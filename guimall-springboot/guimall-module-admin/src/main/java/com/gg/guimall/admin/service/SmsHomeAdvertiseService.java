package com.gg.guimall.admin.service;

import com.gg.guimall.admin.model.vo.sms.CreateSmsHomeAdvertiseReqVO;
import com.gg.guimall.admin.model.vo.sms.FindSmsHomeAdvertiseDetailRspVO;
import com.gg.guimall.admin.model.vo.sms.FindSmsHomeAdvertisePageListReqVO;
import com.gg.guimall.admin.model.vo.sms.FindSmsHomeAdvertisePageListRspVO;
import com.gg.guimall.admin.model.vo.sms.UpdateSmsHomeAdvertiseReqVO;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;

/**
 * 首页轮播广告 Service
 */
public interface SmsHomeAdvertiseService {

    Response createAdvertise(CreateSmsHomeAdvertiseReqVO reqVO);

    PageResponse<FindSmsHomeAdvertisePageListRspVO> findAdvertisePageList(FindSmsHomeAdvertisePageListReqVO reqVO);

    Response findAdvertiseDetail(Long id);

    Response updateAdvertise(UpdateSmsHomeAdvertiseReqVO reqVO);

    Response deleteAdvertise(Long id);
}

