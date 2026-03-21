package com.gg.guimall.admin.service;

import com.gg.guimall.admin.model.vo.sms.CreateSmsHomeNewProductReqVO;
import com.gg.guimall.admin.model.vo.sms.FindSmsHomeNewProductPageListReqVO;
import com.gg.guimall.admin.model.vo.sms.FindSmsHomeNewProductPageListRspVO;
import com.gg.guimall.admin.model.vo.sms.UpdateSmsHomeNewProductReqVO;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;

/**
 * 新品推荐管理 Service
 */
public interface SmsHomeNewProductService {

    Response createNewProduct(CreateSmsHomeNewProductReqVO reqVO);

    PageResponse<FindSmsHomeNewProductPageListRspVO> findNewProductPageList(FindSmsHomeNewProductPageListReqVO reqVO);

    Response findNewProductDetail(Long id);

    Response updateNewProduct(UpdateSmsHomeNewProductReqVO reqVO);

    Response deleteNewProduct(Long id);

    Response findNewProductOptions();
}

