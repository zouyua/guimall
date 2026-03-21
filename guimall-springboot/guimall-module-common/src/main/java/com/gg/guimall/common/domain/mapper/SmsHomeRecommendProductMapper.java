package com.gg.guimall.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.common.domain.dos.SmsHomeRecommendProductDO;

import java.util.List;
import java.util.Objects;

/**
 * 人气推荐商品 Mapper
 *
 * 对应表：sms_home_recommend_product
 */
public interface SmsHomeRecommendProductMapper extends BaseMapper<SmsHomeRecommendProductDO> {

    /**
     * 后台分页查询
     */
    default Page<SmsHomeRecommendProductDO> selectPageList(
            long current,
            long size,
            String productName,
            Integer recommendStatus
    ) {
        Page<SmsHomeRecommendProductDO> page = new Page<>(current, size);

        LambdaQueryWrapper<SmsHomeRecommendProductDO> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .like(Objects.nonNull(productName) && productName.trim().length() > 0,
                        SmsHomeRecommendProductDO::getProductName, productName)
                .eq(Objects.nonNull(recommendStatus),
                        SmsHomeRecommendProductDO::getRecommendStatus, recommendStatus)
                .orderByAsc(SmsHomeRecommendProductDO::getSort);

        return selectPage(page, wrapper);
    }

    /**
     * 前台获取推荐列表（仅推荐）
     */
    default List<SmsHomeRecommendProductDO> selectActiveList() {
        LambdaQueryWrapper<SmsHomeRecommendProductDO> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .eq(SmsHomeRecommendProductDO::getRecommendStatus, 1)
                .orderByAsc(SmsHomeRecommendProductDO::getSort);
        return selectList(wrapper);
    }
}

