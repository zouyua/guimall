package com.gg.guimall.common.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gg.guimall.common.domain.dos.PmsProductDO;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Objects;

/*
 * @author:wly
 * @url:www.gg.com
 * @date:2026/3/10
 * @description:商品Mapper*/
public interface PmsProductMapper extends BaseMapper<PmsProductDO> {

    /**
     * 商品分页查询
     */
    default Page<PmsProductDO> selectPageList(
            long current,
            long size,
            String name,
            Long categoryId,
            Integer publishStatus
    ) {

        Page<PmsProductDO> page = new Page<>(current, size);

        LambdaQueryWrapper<PmsProductDO> wrapper = new LambdaQueryWrapper<>();

        wrapper
                .like(Objects.nonNull(name), PmsProductDO::getName, name)
                .eq(Objects.nonNull(categoryId), PmsProductDO::getProductCategoryId, categoryId)
                .eq(Objects.nonNull(publishStatus), PmsProductDO::getPublishStatus, publishStatus)
                .eq(PmsProductDO::getDeleteStatus, 0)
                .orderByDesc(PmsProductDO::getCreateTime);

        return selectPage(page, wrapper);
    }

    /**
     * 前台：上架商品分页查询（支持关键字/分类/排序）
     *
     * sortType:
     * 0/NULL: 创建时间倒序
     * 1: 销量倒序
     * 2: 销售价格倒序（price）
     */
    default Page<PmsProductDO> selectActivePageList(
            long current,
            long size,
            String keyword,
            Long categoryId,
            Integer sortType
    ) {
        Page<PmsProductDO> page = new Page<>(current, size);

        LambdaQueryWrapper<PmsProductDO> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .like(Objects.nonNull(keyword) && keyword.trim().length() > 0, PmsProductDO::getName, keyword)
                .eq(PmsProductDO::getPublishStatus, 1)
                .eq(PmsProductDO::getDeleteStatus, 0)
                .eq(Objects.nonNull(categoryId), PmsProductDO::getProductCategoryId, categoryId);

        int type = sortType == null ? 0 : sortType;
        switch (type) {
            case 1:
                wrapper.orderByDesc(PmsProductDO::getSale);
                break;
            case 2:
                wrapper.orderByDesc(PmsProductDO::getPrice);
                break;
            case 0:
            default:
                wrapper.orderByDesc(PmsProductDO::getCreateTime);
        }

        return selectPage(page, wrapper);
    }

    /**
     * 商品模糊搜索
     */
    default List<PmsProductDO> selectByKeyword(String keyword) {

        LambdaQueryWrapper<PmsProductDO> wrapper = new LambdaQueryWrapper<>();

        wrapper
                .like(PmsProductDO::getName, keyword)
                .eq(PmsProductDO::getPublishStatus, 1)
                .eq(PmsProductDO::getDeleteStatus, 0)
                .orderByDesc(PmsProductDO::getSale);

        return selectList(wrapper);
    }

    /**
     * 根据分类查询商品
     */
    default List<PmsProductDO> selectByCategoryId(Long categoryId) {

        LambdaQueryWrapper<PmsProductDO> wrapper = new LambdaQueryWrapper<>();

        wrapper
                .eq(PmsProductDO::getProductCategoryId, categoryId)
                .eq(PmsProductDO::getPublishStatus, 1)
                .eq(PmsProductDO::getDeleteStatus, 0)
                .orderByDesc(PmsProductDO::getSale);

        return selectList(wrapper);
    }

    /**
     * 查询上架商品
     */
    default List<PmsProductDO> selectPublishedProducts() {

        LambdaQueryWrapper<PmsProductDO> wrapper = new LambdaQueryWrapper<>();

        wrapper
                .eq(PmsProductDO::getPublishStatus, 1)
                .eq(PmsProductDO::getDeleteStatus, 0)
                .orderByDesc(PmsProductDO::getCreateTime);

        return selectList(wrapper);
    }

}