package com.gg.guimall.admin.service;

import com.gg.guimall.common.utils.Response;

/**
 * 农户-产地关联查询 Service（基于 pms_farmer_origin + pms_product）
 * 用于删除拦截等场景
 *
 * @author wly
 * @date 2026/4/4
 */
public interface TraceProductOriginService {

    /**
     * 根据产地ID查询关联的农户列表（用于删除产地前拦截）
     */
    Response findByOriginId(Long originId);

    /**
     * 根据农户ID查询其名下的商品列表（用于删除农户前拦截）
     */
    Response findByFarmerId(Long farmerId);
}
