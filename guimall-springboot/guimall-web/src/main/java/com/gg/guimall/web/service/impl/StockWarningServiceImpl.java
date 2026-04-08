package com.gg.guimall.web.service.impl;

import com.gg.guimall.common.domain.dos.PmsSkuStockDO;
import com.gg.guimall.common.domain.mapper.PmsSkuStockMapper;
import com.gg.guimall.web.service.StockWarningService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/4/7
 * @description: 库存预警服务实现类
 **/
@Service
@Slf4j
public class StockWarningServiceImpl implements StockWarningService {

    @Autowired
    private PmsSkuStockMapper skuStockMapper;

    @Override
    public void checkStockWarning(Long skuId) {
        if (Objects.isNull(skuId) || skuId <= 0) {
            return;
        }

        try {
            PmsSkuStockDO skuStock = skuStockMapper.selectById(skuId);
            if (Objects.isNull(skuStock)) {
                return;
            }

            // 检查库存是否低于预警值
            if (skuStock.getLowStock() != null && skuStock.getLowStock() > 0) {
                if (skuStock.getStock() <= skuStock.getLowStock()) {
                    log.warn("【库存预警】SKU库存不足! skuId={}, skuCode={}, 当前库存={}, 预警值={}, productId={}",
                            skuStock.getId(),
                            skuStock.getSkuCode(),
                            skuStock.getStock(),
                            skuStock.getLowStock(),
                            skuStock.getProductId());
                    // TODO: 可以在这里发送邮件、短信或系统通知
                }
            }
        } catch (Exception e) {
            log.error("检查库存预警异常, skuId={}", skuId, e);
        }
    }
}
