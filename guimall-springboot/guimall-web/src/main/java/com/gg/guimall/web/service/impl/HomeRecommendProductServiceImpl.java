package com.gg.guimall.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gg.guimall.common.domain.dos.PmsProductDO;
import com.gg.guimall.common.domain.mapper.PmsProductMapper;
import com.gg.guimall.common.utils.Response;
import com.gg.guimall.web.model.vo.home.HomeRecommendProductItemVO;
import com.gg.guimall.web.service.HomeRecommendProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 首页人气推荐 Service 实现类（基于 pms_product.is_recommend 字段）
 */
@Service
@Slf4j
public class HomeRecommendProductServiceImpl implements HomeRecommendProductService {

    @Autowired
    private PmsProductMapper pmsProductMapper;

    @Override
    public Response listRecommendProducts() {
        List<PmsProductDO> list = pmsProductMapper.selectList(
                new LambdaQueryWrapper<PmsProductDO>()
                        .eq(PmsProductDO::getIsRecommend, 1)
                        .eq(PmsProductDO::getPublishStatus, 1)
                        .eq(PmsProductDO::getIsDeleted, 0)
                        .orderByDesc(PmsProductDO::getSale)
                        .last("LIMIT 8")
        );

        if (list == null || list.isEmpty()) {
            return Response.success(Collections.emptyList());
        }

        List<HomeRecommendProductItemVO> voList = list.stream().map(p -> {
            BigDecimal price = p.getPromotionPrice() != null ? p.getPromotionPrice() : p.getPrice();
            HomeRecommendProductItemVO vo = new HomeRecommendProductItemVO();
            vo.setId(p.getId());
            vo.setProductId(p.getId());
            vo.setProductName(p.getName());
            vo.setProductPic(p.getPic());
            vo.setPrice(price);
            vo.setSort(p.getSort());
            return vo;
        }).collect(Collectors.toList());

        return Response.success(voList);
    }
}

