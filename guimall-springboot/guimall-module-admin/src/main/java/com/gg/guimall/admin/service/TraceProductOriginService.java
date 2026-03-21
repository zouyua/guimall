package com.gg.guimall.admin.service;

import com.gg.guimall.admin.model.vo.trace.BindProductOriginReqVO;
import com.gg.guimall.common.utils.Response;

/**
 * 商品产地关联 Service
 *
 * @author wly
 * @url www.gg.com
 * @date 2026/3/19
 */
public interface TraceProductOriginService {

    Response bind(BindProductOriginReqVO reqVO);

    Response findByProductId(Long productId);

    Response unbind(Long productId);
}

