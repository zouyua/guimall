package com.gg.guimall.web.service;

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
}
