package com.gg.guimall.common.domain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.common.domain.dos.SmsHomeNewProductDO;

import java.util.List;
import java.util.Objects;

/**
 * 新品推荐商品 Mapper
 *
 * 对应表：sms_home_new_product
 */
public interface SmsHomeNewProductMapper extends BaseMapper<SmsHomeNewProductDO> {

    /**
     * 后台分页查询
     */
    default Page<SmsHomeNewProductDO> selectPageList(
            long current,
            long size,
            String productName,
            Integer recommendStatus
    ) {
        Page<SmsHomeNewProductDO> page = new Page<>(current, size);

        LambdaQueryWrapper<SmsHomeNewProductDO> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .like(Objects.nonNull(productName) && productName.trim().length() > 0,
                        SmsHomeNewProductDO::getProductName, productName)
                .eq(Objects.nonNull(recommendStatus),
                        SmsHomeNewProductDO::getRecommendStatus, recommendStatus)
                .orderByAsc(SmsHomeNewProductDO::getSort);

        return selectPage(page, wrapper);
    }

    /**
     * 前台获取推荐列表（仅推荐）
     */
    default List<SmsHomeNewProductDO> selectActiveList() {
        LambdaQueryWrapper<SmsHomeNewProductDO> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .eq(SmsHomeNewProductDO::getRecommendStatus, 1)
                .orderByAsc(SmsHomeNewProductDO::getSort);
        return selectList(wrapper);
    }
}

