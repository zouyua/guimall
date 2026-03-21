package com.gg.guimall.admin.service;

import com.gg.guimall.admin.model.vo.sms.*;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;

/**
 * 优惠券管理 Service
 *
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 */
public interface SmsCouponService {

    Response createCoupon(CreateSmsCouponReqVO reqVO);

    PageResponse findCouponPageList(FindSmsCouponPageListReqVO reqVO);

    Response findCouponDetail(Long id);

    Response updateCoupon(UpdateSmsCouponReqVO reqVO);

    Response deleteCoupon(Long id);
}

