package com.gg.guimall.web.service;

import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;
import com.gg.guimall.web.model.vo.pms.FindPmsProductDetailRspVO;
import com.gg.guimall.web.model.vo.pms.FindPmsProductPageListReqVO;
import com.gg.guimall.web.model.vo.pms.ProductPageItemVO;
import com.gg.guimall.web.model.vo.pms.ProductCategoryTreeVO;

import java.util.List;

/**
 * 商品前台服务
 */
public interface PmsProductService {

    List<ProductCategoryTreeVO> findCategoryTree();

    PageResponse<ProductPageItemVO> findProductPageList(FindPmsProductPageListReqVO reqVO);

    Response findProductDetail(Long id);
}

