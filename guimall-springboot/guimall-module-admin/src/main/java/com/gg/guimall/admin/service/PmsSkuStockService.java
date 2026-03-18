package com.gg.guimall.admin.service;

import com.gg.guimall.admin.model.vo.pms.PmsSkuStockVO;
import com.gg.guimall.common.utils.Response;

import java.util.List;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/18
 * @description: SKU库存 Service
 **/
public interface PmsSkuStockService {

    /** 根据商品ID查询SKU列表 */
    Response findSkuListByProductId(Long productId);

    /** 批量保存SKU（先删后插，覆盖更新） */
    Response saveSkuList(Long productId, List<PmsSkuStockVO> skuList);

    /** 删除单个SKU */
    Response deleteSku(Long id);
}
