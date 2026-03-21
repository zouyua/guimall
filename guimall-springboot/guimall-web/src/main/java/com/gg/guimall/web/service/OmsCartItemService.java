package com.gg.guimall.web.service;

import com.gg.guimall.common.utils.Response;
import com.gg.guimall.web.model.vo.cart.*;

/**
 * 购物车 Service
 */
public interface OmsCartItemService {

    Response add(AddCartItemReqVO reqVO);

    Response clear(ClearCartReqVO reqVO);

    Response delete(DeleteCartItemReqVO reqVO);

    Response list(Long memberId);

    Response getProduct(Long productId);

    Response updateAttr(UpdateCartAttrReqVO reqVO);

    Response updateQuantity(Long memberId, Long id, Integer quantity);
}

