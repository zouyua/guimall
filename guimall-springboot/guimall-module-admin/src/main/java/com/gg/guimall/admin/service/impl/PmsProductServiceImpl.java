package com.gg.guimall.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.admin.model.vo.pms.*;
import com.gg.guimall.admin.service.PmsProductService;
import com.gg.guimall.common.domain.dos.PmsProductAttributeCategoryDO;
import com.gg.guimall.common.domain.dos.PmsProductDO;
import com.gg.guimall.common.domain.dos.PmsProductCategoryDO;
import com.gg.guimall.common.domain.dos.PmsFarmerDO;
import com.gg.guimall.common.domain.mapper.PmsFarmerMapper;
import com.gg.guimall.common.domain.mapper.PmsProductAttributeCategoryMapper;
import com.gg.guimall.common.domain.mapper.PmsProductCategoryMapper;
import com.gg.guimall.common.domain.mapper.PmsProductMapper;
import com.gg.guimall.common.enums.ResponseCodeEnum;
import com.gg.guimall.common.exception.BizException;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 商品管理 Service 实现类
 *
 * 负责商品的增删改查
 *
 * @author wly
 */
@Service("adminPmsProductServiceImpl")
@Slf4j
public class PmsProductServiceImpl implements PmsProductService {

    @Autowired
    private PmsProductMapper pmsProductMapper;
    @Autowired
    private PmsProductCategoryMapper pmsProductCategoryMapper;
    @Autowired
    private PmsFarmerMapper pmsFarmerMapper;
    @Autowired
    private PmsProductAttributeCategoryMapper pmsProductAttributeCategoryMapper;

    /**
     * 创建商品
     */
    @Override
    public Response createProduct(PmsProductCreateReqVO reqVO) {
        // 校验商品分类是否存在
        if (Objects.nonNull(reqVO.getProductCategoryId())) {
            PmsProductCategoryDO categoryDO = pmsProductCategoryMapper.selectById(reqVO.getProductCategoryId());
            if (Objects.isNull(categoryDO)) {
                throw new BizException(ResponseCodeEnum.PRODUCT_CATEGORY_NOT_FOUND);
            }
        }

        // 校验农户是否存在
        if (Objects.nonNull(reqVO.getFarmerId())) {
            PmsFarmerDO farmerDO = pmsFarmerMapper.selectById(reqVO.getFarmerId());
            if (Objects.isNull(farmerDO)) {
                throw new BizException(ResponseCodeEnum.FARMER_NOT_FOUND);
            }
        }

        // 校验商品属性分类是否存在（如果传入了）
        if (Objects.nonNull(reqVO.getProductAttributeCategoryId())) {
            PmsProductAttributeCategoryDO attrCategoryDO =
                    pmsProductAttributeCategoryMapper.selectById(reqVO.getProductAttributeCategoryId());
            if (Objects.isNull(attrCategoryDO)) {
                throw new BizException(ResponseCodeEnum.INVALID_PRODUCT_DATA);
            }
        }

        // 构建商品 DO
        PmsProductDO productDO = PmsProductDO.builder()
                .productCategoryId(reqVO.getProductCategoryId())
                .productAttributeCategoryId(reqVO.getProductAttributeCategoryId())
                .farmerId(reqVO.getFarmerId())
                .name(reqVO.getName())
                .subTitle(reqVO.getSubTitle())
                .productSn(reqVO.getProductSn())
                .pic(reqVO.getPic())
                .albumPics(reqVO.getAlbumPics())
                .description(reqVO.getDescription())
                .price(reqVO.getPrice())
                .marketPrice(reqVO.getMarketPrice())
                .stock(reqVO.getStock())
                .unit(reqVO.getUnit())
                .weight(reqVO.getWeight())
                .keywords(reqVO.getKeywords())
                .note(reqVO.getNote())
                .detailHtml(reqVO.getDetailHtml())
                .publishStatus(0) // 默认下架状态
                .isNew(reqVO.getIsNew() != null ? reqVO.getIsNew() : 0)
                .isRecommend(reqVO.getIsRecommend() != null ? reqVO.getIsRecommend() : 0)
                .isDeleted(0) // 默认未删除
                .sort(reqVO.getSort() != null ? reqVO.getSort() : 0)
                .build();

        // 插入数据库
        pmsProductMapper.insert(productDO);

        return Response.success();
    }

    /**
     * 商品分页查询（列表项含关联展示：分类名、农户名、属性分类名）
     */
    @Override
    public PageResponse findProductPageList(FindPmsProductPageListReqVO reqVO) {

        Page<PmsProductDO> page = pmsProductMapper.selectPageList(
                reqVO.getCurrent(),
                reqVO.getSize(),
                reqVO.getName(),
                reqVO.getCategoryId(),
                reqVO.getPublishStatus()
        );

        if (page.getRecords().isEmpty()) {
            return PageResponse.success(page, Collections.emptyList());
        }

        // 批量查询分类名
        List<Long> catIds = page.getRecords().stream()
                .map(PmsProductDO::getProductCategoryId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<Long, String> catNameMap = catIds.isEmpty() ? Collections.emptyMap() :
                pmsProductCategoryMapper.selectBatchIds(catIds).stream()
                        .collect(Collectors.toMap(PmsProductCategoryDO::getId, PmsProductCategoryDO::getName));

        // 批量查询农户名
        List<Long> farmerIds = page.getRecords().stream()
                .map(PmsProductDO::getFarmerId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<Long, String> farmerNameMap = farmerIds.isEmpty() ? Collections.emptyMap() :
                pmsFarmerMapper.selectBatchIds(farmerIds).stream()
                        .collect(Collectors.toMap(PmsFarmerDO::getId, PmsFarmerDO::getName));

        // 批量查询属性分类名
        List<Long> attrCatIds = page.getRecords().stream()
                .map(PmsProductDO::getProductAttributeCategoryId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<Long, String> attrCatNameMap = attrCatIds.isEmpty() ? Collections.emptyMap() :
                pmsProductAttributeCategoryMapper.selectBatchIds(attrCatIds).stream()
                        .collect(Collectors.toMap(PmsProductAttributeCategoryDO::getId, PmsProductAttributeCategoryDO::getName));

        List<FindPmsProductPageListRspVO> voList = page.getRecords().stream()
                .map(product -> FindPmsProductPageListRspVO.builder()
                        .id(product.getId())
                        .productCategoryId(product.getProductCategoryId())
                        .categoryName(catNameMap.get(product.getProductCategoryId()))
                        .productAttributeCategoryId(product.getProductAttributeCategoryId())
                        .productAttributeCategoryName(attrCatNameMap.get(product.getProductAttributeCategoryId()))
                        .farmerId(product.getFarmerId())
                        .farmerName(farmerNameMap.get(product.getFarmerId()))
                        .name(product.getName())
                        .subTitle(product.getSubTitle())
                        .productSn(product.getProductSn())
                        .pic(product.getPic())
                        .price(product.getPrice())
                        .stock(product.getStock())
                        .publishStatus(product.getPublishStatus())
                        .sale(product.getSale())
                        .createTime(product.getCreateTime())
                        .build())
                .collect(Collectors.toList());

        return PageResponse.success(page, voList);
    }

    /**
     * 查询商品详情（含关联展示：分类名称、农户名称、属性分类名称）
     */
    @Override
    public Response findProductDetail(Long id) {

        if (Objects.isNull(id) || id <= 0) {
            throw new BizException(ResponseCodeEnum.INVALID_PRODUCT_DATA);
        }

        PmsProductDO productDO = pmsProductMapper.selectById(id);

        if (Objects.isNull(productDO)) {
            throw new BizException(ResponseCodeEnum.PRODUCT_NOT_FOUND);
        }

        FindPmsProductDetailRspVO rspVO = new FindPmsProductDetailRspVO();
        BeanUtils.copyProperties(productDO, rspVO);

        // 关联查询并填充分类名称
        if (Objects.nonNull(productDO.getProductCategoryId())) {
            PmsProductCategoryDO category = pmsProductCategoryMapper.selectById(productDO.getProductCategoryId());
            if (category != null) {
                rspVO.setCategoryName(category.getName());
            }
        }

        // 关联查询并填充农户名称
        if (Objects.nonNull(productDO.getFarmerId())) {
            PmsFarmerDO farmer = pmsFarmerMapper.selectById(productDO.getFarmerId());
            if (farmer != null) {
                rspVO.setFarmerName(farmer.getName());
            }
        }

        // 关联查询并填充属性分类名称
        if (Objects.nonNull(productDO.getProductAttributeCategoryId())) {
            PmsProductAttributeCategoryDO attrCategory = pmsProductAttributeCategoryMapper.selectById(productDO.getProductAttributeCategoryId());
            if (attrCategory != null) {
                rspVO.setProductAttributeCategoryName(attrCategory.getName());
            }
        }

        return Response.success(rspVO);
    }

    /**
     * 修改商品
     */
    @Override
    public Response updateProduct(PmsProductUpdateReqVO reqVO) {
        // 参数校验
        if (Objects.isNull(reqVO.getId()) || reqVO.getId() <= 0) {
            throw new BizException(ResponseCodeEnum.INVALID_PRODUCT_DATA);
        }

        // 校验商品是否存在
        PmsProductDO existProduct = pmsProductMapper.selectById(reqVO.getId());
        if (Objects.isNull(existProduct)) {
            throw new BizException(ResponseCodeEnum.PRODUCT_NOT_FOUND);
        }

        // 校验商品分类是否存在
        if (Objects.nonNull(reqVO.getProductCategoryId())) {
            PmsProductCategoryDO categoryDO = pmsProductCategoryMapper.selectById(reqVO.getProductCategoryId());
            if (Objects.isNull(categoryDO)) {
                throw new BizException(ResponseCodeEnum.PRODUCT_CATEGORY_NOT_FOUND);
            }
        }

        // 校验农户是否存在
        if (Objects.nonNull(reqVO.getFarmerId())) {
            PmsFarmerDO farmerDO = pmsFarmerMapper.selectById(reqVO.getFarmerId());
            if (Objects.isNull(farmerDO)) {
                throw new BizException(ResponseCodeEnum.FARMER_NOT_FOUND);
            }
        }

        // 校验商品属性分类是否存在（如果传入了）
        if (Objects.nonNull(reqVO.getProductAttributeCategoryId())) {
            PmsProductAttributeCategoryDO attrCategoryDO =
                    pmsProductAttributeCategoryMapper.selectById(reqVO.getProductAttributeCategoryId());
            if (Objects.isNull(attrCategoryDO)) {
                throw new BizException(ResponseCodeEnum.INVALID_PRODUCT_DATA);
            }
        }

        PmsProductDO productDO = PmsProductDO.builder()
                .id(reqVO.getId())
                .productCategoryId(reqVO.getProductCategoryId())
                .productAttributeCategoryId(reqVO.getProductAttributeCategoryId())
                .farmerId(reqVO.getFarmerId())
                .name(reqVO.getName())
                .subTitle(reqVO.getSubTitle())
                .productSn(reqVO.getProductSn())
                .pic(reqVO.getPic())
                .albumPics(reqVO.getAlbumPics())
                .description(reqVO.getDescription())
                .price(reqVO.getPrice())
                .marketPrice(reqVO.getMarketPrice())
                .stock(reqVO.getStock())
                .unit(reqVO.getUnit())
                .weight(reqVO.getWeight())
                .keywords(reqVO.getKeywords())
                .note(reqVO.getNote())
                .detailHtml(reqVO.getDetailHtml())
                .isNew(reqVO.getIsNew())
                .isRecommend(reqVO.getIsRecommend())
                .publishStatus(reqVO.getPublishStatus())
                .sort(reqVO.getSort())
                .updateTime(LocalDateTime.now())
                .build();

        pmsProductMapper.updateById(productDO);

        return Response.success();
    }

    /**
     * 删除商品
     */
    @Override
    public Response deleteProduct(Long id) {

        // 参数校验
        if (Objects.isNull(id) || id <= 0) {
            throw new BizException(ResponseCodeEnum.INVALID_PRODUCT_DATA);
        }

        // 校验商品是否存在
        PmsProductDO productDO = pmsProductMapper.selectById(id);
        if (Objects.isNull(productDO)) {
            throw new BizException(ResponseCodeEnum.PRODUCT_NOT_FOUND);
        }

        // 执行删除
        pmsProductMapper.deleteById(id);

        return Response.success();
    }

    /**
     * 上架商品
     */
    @Override
    public Response publishProduct(Long id) {
        return updatePublishStatus(id, 1);
    }

    /**
     * 下架商品
     */
    @Override
    public Response unpublishProduct(Long id) {
        return updatePublishStatus(id, 0);
    }

    /**
     * 更新上架状态
     */
    private Response updatePublishStatus(Long id, Integer publishStatus) {
        // 参数校验
        if (Objects.isNull(id) || id <= 0) {
            throw new BizException(ResponseCodeEnum.INVALID_PRODUCT_DATA);
        }

        // 校验商品是否存在
        PmsProductDO productDO = pmsProductMapper.selectById(id);
        if (Objects.isNull(productDO)) {
            throw new BizException(ResponseCodeEnum.PRODUCT_NOT_FOUND);
        }

        // 更新上架状态
        PmsProductDO updateDO = PmsProductDO.builder()
                .id(id)
                .publishStatus(publishStatus)
                .build();
        pmsProductMapper.updateById(updateDO);

        return Response.success();
    }
}