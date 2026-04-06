package com.gg.guimall.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gg.guimall.common.domain.dos.PmsProductDO;
import com.gg.guimall.common.domain.mapper.PmsProductMapper;
import com.gg.guimall.common.utils.Response;
import com.gg.guimall.web.model.vo.home.HomeNewProductItemVO;
import com.gg.guimall.web.service.HomeNewProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 首页新品推荐 Service 实现类（基于 pms_product.is_new 字段）
 */
@Service
@Slf4j
public class HomeNewProductServiceImpl implements HomeNewProductService {

    @Autowired
    private PmsProductMapper pmsProductMapper;

    @Override
    public Response listNewProducts() {
        List<PmsProductDO> list = pmsProductMapper.selectList(
                new LambdaQueryWrapper<PmsProductDO>()
                        .eq(PmsProductDO::getIsNew, 1)
                        .eq(PmsProductDO::getPublishStatus, 1)
                        .eq(PmsProductDO::getIsDeleted, 0)
                        .orderByDesc(PmsProductDO::getSort)
                        .last("LIMIT 8")
        );

        if (list == null || list.isEmpty()) {
            return Response.success(Collections.emptyList());
        }

        List<HomeNewProductItemVO> voList = list.stream().map(p -> {
            BigDecimal price = p.getPromotionPrice() != null ? p.getPromotionPrice() : p.getPrice();
            return HomeNewProductItemVO.builder()
                    .id(p.getId())
                    .productId(p.getId())
                    .productName(p.getName())
                    .productPic(p.getPic())
                    .price(price)
                    .sort(p.getSort())
                    .build();
        }).collect(Collectors.toList());

        return Response.success(voList);
    }
}

