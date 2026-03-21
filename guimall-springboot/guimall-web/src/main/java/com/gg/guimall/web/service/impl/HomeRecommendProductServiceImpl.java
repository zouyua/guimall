package com.gg.guimall.web.service.impl;

import com.gg.guimall.common.domain.dos.PmsProductDO;
import com.gg.guimall.common.domain.dos.SmsHomeRecommendProductDO;
import com.gg.guimall.common.domain.mapper.PmsProductMapper;
import com.gg.guimall.common.domain.mapper.SmsHomeRecommendProductMapper;
import com.gg.guimall.common.utils.Response;
import com.gg.guimall.web.model.vo.home.HomeRecommendProductItemVO;
import com.gg.guimall.web.service.HomeRecommendProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 首页人气推荐 Service 实现类
 */
@Service
@Slf4j
public class HomeRecommendProductServiceImpl implements HomeRecommendProductService {

    @Autowired
    private SmsHomeRecommendProductMapper smsHomeRecommendProductMapper;

    @Autowired
    private PmsProductMapper pmsProductMapper;

    @Override
    public Response listRecommendProducts() {
        List<SmsHomeRecommendProductDO> list = smsHomeRecommendProductMapper.selectActiveList();
        if (Objects.isNull(list) || list.isEmpty()) {
            return Response.success(Collections.emptyList());
        }

        List<HomeRecommendProductItemVO> voList = list.stream()
                .map(item -> {
                    if (Objects.isNull(item) || Objects.isNull(item.getProductId())) {
                        return null;
                    }

                    PmsProductDO productDO = pmsProductMapper.selectById(item.getProductId());
                    if (Objects.isNull(productDO) ||
                            !Objects.equals(productDO.getDeleteStatus(), 0) ||
                            !Objects.equals(productDO.getPublishStatus(), 1)) {
                        return null;
                    }

                    BigDecimal price = productDO.getPromotionPrice() != null
                            ? productDO.getPromotionPrice()
                            : productDO.getPrice();

                    HomeRecommendProductItemVO vo = new HomeRecommendProductItemVO();
                    vo.setId(item.getId());
                    vo.setProductId(productDO.getId());
                    vo.setProductName(Objects.nonNull(item.getProductName()) ? item.getProductName() : productDO.getName());
                    vo.setProductPic(productDO.getPic());
                    vo.setPrice(price);
                    vo.setSort(item.getSort());
                    return vo;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return Response.success(voList);
    }
}

