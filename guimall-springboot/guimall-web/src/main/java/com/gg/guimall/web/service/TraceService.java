package com.gg.guimall.web.service;

import com.gg.guimall.common.utils.Response;

/**
 * 溯源查询 Service（前台接口）
 *
 * @author wly
 * @url www.gg.com
 * @date 2026/3/19
 */
public interface TraceService {

    /**
     * 根据商品ID查询溯源详情（并累加扫码次数）
     */
    Response findTraceDetail(Long productId);
}

