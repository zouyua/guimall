package com.gg.guimall.web.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.common.domain.dos.PmsFarmerDO;
import com.gg.guimall.common.domain.dos.PmsProductCategoryDO;
import com.gg.guimall.common.domain.dos.PmsProductDO;
import com.gg.guimall.common.domain.dos.PmsSkuStockDO;
import com.gg.guimall.common.domain.dos.PmsProductAttributeCategoryDO;
import com.gg.guimall.common.domain.mapper.PmsFarmerMapper;
import com.gg.guimall.common.domain.mapper.PmsProductAttributeCategoryMapper;
import com.gg.guimall.common.domain.mapper.PmsProductCategoryMapper;
import com.gg.guimall.common.domain.mapper.PmsProductMapper;
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

    // 这里并不强依赖属性分类名称，但保留字段扩展时可用
    @Autowired(required = false)
    private PmsProductAttributeCategoryMapper pmsProductAttributeCategoryMapper;

    @Override
    public List<ProductCategoryTreeVO> findCategoryTree() {
        List<PmsProductCategoryDO> list = pmsProductCategoryMapper.selectList(null);
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
        Page<PmsProductDO> page = pmsProductMapper.selectActivePageList(
                reqVO.getCurrent(),
                reqVO.getSize(),
                reqVO.getKeyword(),
                reqVO.getCategoryId(),
                reqVO.getSortType()
        );

        List<ProductPageItemVO> voList = page.getRecords().stream()
                .map(this::convertToPageItemVO)
                .collect(Collectors.toList());

        return PageResponse.success(page, voList);
    }

    private ProductPageItemVO convertToPageItemVO(PmsProductDO product) {
        if (Objects.isNull(product)) {
            return null;
        }

        ProductPageItemVO vo = new ProductPageItemVO();
        vo.setId(product.getId());
        vo.setProductCategoryId(product.getProductCategoryId());
        vo.setFarmerId(product.getFarmerId());
        vo.setName(product.getName());
        vo.setSubTitle(product.getSubTitle());
        vo.setPic(product.getPic());
        vo.setPrice(product.getPromotionPrice() != null ? product.getPromotionPrice() : product.getPrice());
        vo.setStock(product.getStock());
        vo.setPublishStatus(product.getPublishStatus());
        vo.setSale(product.getSale());
        vo.setCreateTime(product.getCreateTime());

        if (Objects.nonNull(product.getProductCategoryId())) {
            PmsProductCategoryDO category = pmsProductCategoryMapper.selectById(product.getProductCategoryId());
            if (Objects.nonNull(category)) {
                vo.setCategoryName(category.getName());
            }
        }
        if (Objects.nonNull(product.getFarmerId())) {
            PmsFarmerDO farmer = pmsFarmerMapper.selectById(product.getFarmerId());
            if (Objects.nonNull(farmer)) {
                vo.setFarmerName(farmer.getName());
            }
        }

        return vo;
    }

    @Override
    public Response findProductDetail(Long id) {
        if (Objects.isNull(id) || id <= 0) {
            throw new BizException(ResponseCodeEnum.INVALID_PRODUCT_DATA);
        }

        PmsProductDO productDO = pmsProductMapper.selectById(id);
        if (Objects.isNull(productDO) || Objects.equals(productDO.getDeleteStatus(), 1)) {
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

        // SKU 列表
        List<PmsSkuStockDO> skuList = pmsSkuStockMapper.selectByProductId(id);
        if (Objects.isNull(skuList) || skuList.isEmpty()) {
            rspVO.setSkus(Collections.emptyList());
        } else {
            List<ProductSkuStockItemVO> skuVOList = skuList.stream()
                    .map(sku -> {
                        if (Objects.isNull(sku)) {
                            return null;
                        }
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
                        vo.setSpData(sku.getSpData());
                        return vo;
                    })
                    .filter(Objects::nonNull)
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

