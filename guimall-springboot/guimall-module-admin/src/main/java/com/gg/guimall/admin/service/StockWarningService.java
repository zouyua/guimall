package com.gg.guimall.admin.service;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/4/7
 * @description: 库存预警服务
 **/
public interface StockWarningService {

    /**
     * 检查SKU库存是否低于预警值
     * @param skuId SKU ID
     */
    void checkStockWarning(Long skuId);

    /**
     * 批量检查商品下所有SKU的库存预警
     * @param productId 商品ID
     */
    void checkProductStockWarning(Long productId);
}
