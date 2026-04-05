package com.gg.guimall.web.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.common.domain.dos.PmsFarmerDO;
import com.gg.guimall.common.domain.dos.PmsProductCategoryDO;
import com.gg.guimall.common.domain.dos.PmsProductDO;
import com.gg.guimall.common.domain.dos.PmsProductParamDO;
import com.gg.guimall.common.domain.dos.PmsParamDefinitionDO;
import com.gg.guimall.common.domain.dos.PmsSkuSpecDO;
import com.gg.guimall.common.domain.dos.PmsSkuStockDO;
import com.gg.guimall.common.domain.dos.PmsFarmerDO;
import com.gg.guimall.common.domain.mapper.PmsFarmerMapper;
import com.gg.guimall.common.domain.mapper.PmsProductCategoryMapper;
import com.gg.guimall.common.domain.mapper.PmsProductMapper;
import com.gg.guimall.common.domain.mapper.PmsProductParamMapper;
import com.gg.guimall.common.domain.mapper.PmsParamDefinitionMapper;
import com.gg.guimall.common.domain.mapper.PmsSkuSpecMapper;
import com.gg.guimall.common.domain.mapper.PmsSkuStockMapper;
import com.gg.guimall.common.enums.ResponseCodeEnum;
import com.gg.guimall.common.exception.BizException;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;
import com.gg.guimall.web.model.vo.pms.*;
import com.gg.guimall.web.service.PmsProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 商品前台服务实现类
 */
@Service("webPmsProductServiceImpl")
public class PmsProductServiceImpl implements PmsProductService {

    @Autowired
    private PmsProductMapper pmsProductMapper;

    @Autowired
    private PmsProductCategoryMapper pmsProductCategoryMapper;

    @Autowired
    private PmsFarmerMapper pmsFarmerMapper;

    @Autowired
    private PmsSkuStockMapper pmsSkuStockMapper;

    @Autowired
    private PmsSkuSpecMapper pmsSkuSpecMapper;

    @Autowired
    private PmsProductParamMapper pmsProductParamMapper;

    @Autowired
    private PmsParamDefinitionMapper pmsParamDefinitionMapper;

    @Override
    public List<ProductCategoryTreeVO> findCategoryTree() {
        // 只查询 show_status 为 1 的分类，并按 sort 排序
        List<PmsProductCategoryDO> list = pmsProductCategoryMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<PmsProductCategoryDO>()
                        .eq(PmsProductCategoryDO::getStatus, 1)
                        .orderByAsc(PmsProductCategoryDO::getSort)
        );
        if (Objects.isNull(list) || list.isEmpty()) {
            return Collections.emptyList();
        }

        List<ProductCategoryTreeVO> voList = list.stream().map(item -> {
            ProductCategoryTreeVO vo = new ProductCategoryTreeVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).collect(Collectors.toList());

        return buildTree(voList);
    }

    private List<ProductCategoryTreeVO> buildTree(List<ProductCategoryTreeVO> allCategories) {
        Map<Long, ProductCategoryTreeVO> categoryMap = allCategories.stream()
                .collect(Collectors.toMap(ProductCategoryTreeVO::getId, c -> c, (a, b) -> a));

        List<ProductCategoryTreeVO> roots = new ArrayList<>();
        for (ProductCategoryTreeVO category : allCategories) {
            Long parentId = category.getParentId();
            if (Objects.isNull(parentId) || Objects.equals(parentId, 0L)) {
                roots.add(category);
            } else {
                ProductCategoryTreeVO parent = categoryMap.get(parentId);
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(category);
                }
            }
        }
        return roots;
    }

    @Override
    public PageResponse<ProductPageItemVO> findProductPageList(FindPmsProductPageListReqVO reqVO) {
        // 预查询分类ID列表（含子分类），避免 inSql 子查询
        List<Long> categoryIds = null;
        if (Objects.nonNull(reqVO.getCategoryId())) {
            categoryIds = pmsProductCategoryMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<PmsProductCategoryDO>()
                    .eq(PmsProductCategoryDO::getParentId, reqVO.getCategoryId())
            ).stream().map(PmsProductCategoryDO::getId).collect(Collectors.toList());
            categoryIds.add(reqVO.getCategoryId());
        }

        Page<PmsProductDO> page = pmsProductMapper.selectActivePageList(
                reqVO.getCurrent(),
                reqVO.getSize(),
                reqVO.getKeyword(),
                categoryIds,
                reqVO.getSortType(),
                reqVO.getIsAidAgriculture()
        );

        if (page.getRecords().isEmpty()) {
            return PageResponse.success(page, Collections.emptyList());
        }

        // 批量查询分类信息
        List<Long> pageCategoryIds = page.getRecords().stream()
                .map(PmsProductDO::getProductCategoryId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, String> categoryNameMap = Collections.emptyMap();
        if (!pageCategoryIds.isEmpty()) {
            categoryNameMap = pmsProductCategoryMapper.selectBatchIds(pageCategoryIds).stream()
                    .collect(Collectors.toMap(PmsProductCategoryDO::getId, PmsProductCategoryDO::getName));
        }

        // 批量查询农户信息
        List<Long> farmerIds = page.getRecords().stream()
                .map(PmsProductDO::getFarmerId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, String> farmerNameMap = Collections.emptyMap();
        if (!farmerIds.isEmpty()) {
            farmerNameMap = pmsFarmerMapper.selectBatchIds(farmerIds).stream()
                    .collect(Collectors.toMap(PmsFarmerDO::getId, PmsFarmerDO::getName));
        }

        final Map<Long, String> finalCategoryNameMap = categoryNameMap;
        final Map<Long, String> finalFarmerNameMap = farmerNameMap;

        List<ProductPageItemVO> voList = page.getRecords().stream()
                .map(product -> ProductPageItemVO.builder()
                        .id(product.getId())
                        .productCategoryId(product.getProductCategoryId())
                        .categoryName(finalCategoryNameMap.get(product.getProductCategoryId()))
                        .farmerId(product.getFarmerId())
                        .farmerName(finalFarmerNameMap.get(product.getFarmerId()))
                        .name(product.getName())
                        .subTitle(product.getSubTitle())
                        .pic(product.getPic())
                        .price(product.getPromotionPrice() != null ? product.getPromotionPrice() : product.getPrice())
                        .stock(product.getStock())
                        .publishStatus(product.getPublishStatus())
                        .sale(product.getSale())
                        .createTime(product.getCreateTime())
                        .build())
                .collect(Collectors.toList());

        return PageResponse.success(page, voList);
    }

    @Override
    public Response findProductDetail(Long id) {
        if (Objects.isNull(id) || id <= 0) {
            throw new BizException(ResponseCodeEnum.INVALID_PRODUCT_DATA);
        }

        PmsProductDO productDO = pmsProductMapper.selectById(id);
        if (Objects.isNull(productDO) || Objects.equals(productDO.getIsDeleted(), 1)) {
            throw new BizException(ResponseCodeEnum.PRODUCT_NOT_FOUND);
        }
        if (!Objects.equals(productDO.getPublishStatus(), 1)) {
            // 下架商品不返回详情，避免前台显示异常
            throw new BizException(ResponseCodeEnum.PRODUCT_NOT_FOUND);
        }

        FindPmsProductDetailRspVO rspVO = new FindPmsProductDetailRspVO();
        BeanUtils.copyProperties(productDO, rspVO);

        // 分类名/农户名补充
        if (Objects.nonNull(productDO.getProductCategoryId())) {
            PmsProductCategoryDO category = pmsProductCategoryMapper.selectById(productDO.getProductCategoryId());
            if (category != null) {
                rspVO.setCategoryName(category.getName());
            }
        }
        if (Objects.nonNull(productDO.getFarmerId())) {
            PmsFarmerDO farmer = pmsFarmerMapper.selectById(productDO.getFarmerId());
            if (farmer != null) {
                rspVO.setFarmerName(farmer.getName());
            }
        }

        // 查询商品参数（关联参数定义表拿参数名）
        List<PmsProductParamDO> paramDOs = pmsProductParamMapper.selectByProductId(id);
        if (paramDOs != null && !paramDOs.isEmpty()) {
            List<Long> paramIds = paramDOs.stream()
                    .map(PmsProductParamDO::getParamId).distinct().collect(Collectors.toList());
            Map<Long, String> paramNameMap = paramIds.isEmpty() ? Collections.emptyMap() :
                    pmsParamDefinitionMapper.selectBatchIds(paramIds).stream()
                            .collect(Collectors.toMap(PmsParamDefinitionDO::getId, PmsParamDefinitionDO::getParamName));

            List<ProductParamItemVO> paramVOs = paramDOs.stream()
                    .map(p -> ProductParamItemVO.builder()
                            .paramId(p.getParamId())
                            .key(paramNameMap.getOrDefault(p.getParamId(), ""))
                            .value(p.getParamValue())
                            .build())
                    .collect(Collectors.toList());
            rspVO.setProductParams(paramVOs);
        }

        // SKU 列表（含规格明细）
        List<PmsSkuStockDO> skuList = pmsSkuStockMapper.selectByProductId(id);
        if (Objects.isNull(skuList) || skuList.isEmpty()) {
            rspVO.setSkus(Collections.emptyList());
        } else {
            // 批量查询规格，按 skuId 分组
            List<PmsSkuSpecDO> allSpecs = pmsSkuSpecMapper.selectByProductId(id);
            java.util.Map<Long, List<com.gg.guimall.web.model.vo.pms.SkuSpecItemVO>> specMap = allSpecs.stream()
                    .collect(Collectors.groupingBy(PmsSkuSpecDO::getSkuId,
                            Collectors.mapping(s -> com.gg.guimall.web.model.vo.pms.SkuSpecItemVO.builder()
                                    .specKey(s.getSpecKey())
                                    .specValue(s.getSpecValue())
                                    .sort(s.getSort())
                                    .build(), Collectors.toList())));

            List<ProductSkuStockItemVO> skuVOList = skuList.stream()
                    .filter(Objects::nonNull)
                    .map(sku -> {
                        ProductSkuStockItemVO vo = new ProductSkuStockItemVO();
                        vo.setId(sku.getId());
                        vo.setProductId(sku.getProductId());
                        vo.setSkuCode(sku.getSkuCode());
                        vo.setPrice(sku.getPrice());
                        vo.setPromotionPrice(sku.getPromotionPrice());
                        vo.setStock(sku.getStock());
                        vo.setLowStock(sku.getLowStock());
                        vo.setLockStock(sku.getLockStock());
                        vo.setPic(sku.getPic());
                        vo.setSale(sku.getSale());
                        vo.setSpecs(specMap.getOrDefault(sku.getId(), Collections.emptyList()));
                        return vo;
                    })
                    .collect(Collectors.toList());
            rspVO.setSkus(skuVOList);
        }

        // 前台通常展示“促销价”，这里统一一下 price 字段展示口径
        if (productDO.getPromotionPrice() != null) {
            rspVO.setPrice(productDO.getPromotionPrice());
        }

        return Response.success(rspVO);
    }
}

