package com.gg.guimall.admin.service;

import com.gg.guimall.admin.model.vo.sms.CreateSmsHomeRecommendProductReqVO;
import com.gg.guimall.admin.model.vo.sms.FindSmsHomeRecommendProductDetailRspVO;
import com.gg.guimall.admin.model.vo.sms.FindSmsHomeRecommendProductPageListReqVO;
import com.gg.guimall.admin.model.vo.sms.FindSmsHomeRecommendProductPageListRspVO;
import com.gg.guimall.admin.model.vo.sms.UpdateSmsHomeRecommendProductReqVO;
import com.gg.guimall.admin.model.vo.sms.HomeRecommendProductOptionVO;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;

/**
 * 人气推荐管理 Service
 */
public interface SmsHomeRecommendProductService {

    Response createRecommendProduct(CreateSmsHomeRecommendProductReqVO reqVO);

    PageResponse<FindSmsHomeRecommendProductPageListRspVO> findRecommendProductPageList(FindSmsHomeRecommendProductPageListReqVO reqVO);

    Response findRecommendProductDetail(Long id);

    Response updateRecommendProduct(UpdateSmsHomeRecommendProductReqVO reqVO);

    Response deleteRecommendProduct(Long id);

    Response findRecommendProductOptions();
}

