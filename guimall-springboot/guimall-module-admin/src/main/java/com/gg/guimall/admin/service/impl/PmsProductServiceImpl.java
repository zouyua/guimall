package com.gg.guimall.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.admin.model.vo.pms.*;
import com.gg.guimall.admin.service.PmsProductService;
import com.gg.guimall.admin.service.TraceQrcodeService;
import com.gg.guimall.common.domain.dos.PmsProductDO;
import com.gg.guimall.common.domain.dos.PmsProductCategoryDO;
import com.gg.guimall.common.domain.dos.PmsProductParamDO;
import com.gg.guimall.common.domain.dos.PmsParamDefinitionDO;
import com.gg.guimall.common.domain.dos.PmsFarmerDO;
import com.gg.guimall.common.domain.dos.PmsSkuStockDO;
import com.gg.guimall.common.domain.mapper.PmsFarmerMapper;
import com.gg.guimall.common.domain.mapper.PmsProductCategoryMapper;
import com.gg.guimall.common.domain.mapper.PmsProductMapper;
import com.gg.guimall.common.domain.mapper.PmsProductParamMapper;
import com.gg.guimall.common.domain.mapper.PmsParamDefinitionMapper;
import com.gg.guimall.common.domain.mapper.PmsSkuStockMapper;
import com.gg.guimall.common.enums.ResponseCodeEnum;
import com.gg.guimall.common.exception.BizException;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
    private PmsProductParamMapper pmsProductParamMapper;
    @Autowired
    private PmsParamDefinitionMapper pmsParamDefinitionMapper;
    @Autowired
    private PmsSkuStockMapper pmsSkuStockMapper;
    @Autowired
    private TraceQrcodeService traceQrcodeService;

    /**
     * 创建商品
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
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

        // 构建商品 DO
        PmsProductDO productDO = PmsProductDO.builder()
                .productCategoryId(reqVO.getProductCategoryId())
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

        // 保存商品参数
        saveProductParams(productDO.getId(), reqVO.getProductParams());

        // 保存SKU库存
        saveSkuStockList(productDO.getId(), reqVO.getSkuStockList());

        // 自动生成溯源二维码
        try {
            traceQrcodeService.generate(productDO.getId());
            log.info("商品创建成功，已自动生成溯源二维码，productId: {}", productDO.getId());
        } catch (Exception e) {
            log.error("自动生成溯源二维码失败，productId: {}", productDO.getId(), e);
            // 不影响商品创建，只记录日志
        }

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

        List<FindPmsProductPageListRspVO> voList = page.getRecords().stream()
                .map(product -> FindPmsProductPageListRspVO.builder()
                        .id(product.getId())
                        .productCategoryId(product.getProductCategoryId())
                        .categoryName(catNameMap.get(product.getProductCategoryId()))
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
     * 查询商品详情（含关联展示：分类名称、农户名称）
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

        // 查询商品参数（关联参数定义表拿参数名和参数值）
        List<PmsProductParamDO> paramDOs = pmsProductParamMapper.selectByProductId(id);
        if (!CollectionUtils.isEmpty(paramDOs)) {
            // 批量查询参数定义（包含参数名和参数值）
            List<Long> paramIds = paramDOs.stream()
                    .map(PmsProductParamDO::getParamId).distinct().collect(Collectors.toList());
            Map<Long, PmsParamDefinitionDO> paramDefMap = paramIds.isEmpty() ? Collections.emptyMap() :
                    pmsParamDefinitionMapper.selectBatchIds(paramIds).stream()
                            .collect(Collectors.toMap(PmsParamDefinitionDO::getId, p -> p));

            List<ProductParamItemVO> paramVOs = paramDOs.stream()
                    .map(p -> {
                        PmsParamDefinitionDO def = paramDefMap.get(p.getParamId());
                        return ProductParamItemVO.builder()
                                .paramId(p.getParamId())
                                .key(def != null ? def.getParamName() : "")
                                .value(def != null ? def.getParamValue() : "")
                                .build();
                    })
                    .collect(Collectors.toList());
            rspVO.setProductParams(paramVOs);
        }

        return Response.success(rspVO);
    }

    /**
     * 修改商品
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
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

        PmsProductDO productDO = PmsProductDO.builder()
                .id(reqVO.getId())
                .productCategoryId(reqVO.getProductCategoryId())
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

        // 更新商品参数（先删后插）
        saveProductParams(reqVO.getId(), reqVO.getProductParams());

        return Response.success();
    }

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

    /**
     * 批量删除商品
     */
    @Override
    public Response batchDelete(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }
        pmsProductMapper.deleteBatchIds(ids);
        return Response.success();
    }

    /**
     * 批量更新上架状态
     */
    @Override
    public Response batchUpdatePublishStatus(List<Long> ids, Integer publishStatus) {
        if (ids == null || ids.isEmpty()) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }
        for (Long id : ids) {
            PmsProductDO update = new PmsProductDO();
            update.setId(id);
            update.setPublishStatus(publishStatus);
            pmsProductMapper.updateById(update);
        }
        return Response.success();
    }

    /**
     * 保存商品参数（先删后插）
     */
    private void saveProductParams(Long productId, List<ProductParamItemVO> productParams) {
        // 先删除旧参数
        pmsProductParamMapper.deleteByProductId(productId);

        if (CollectionUtils.isEmpty(productParams)) {
            System.out.println("商品参数为空，跳过保存");
            return;
        }

        System.out.println("开始保存商品参数，商品ID: " + productId + ", 参数数量: " + productParams.size());

        // 批量插入新参数
        for (int i = 0; i < productParams.size(); i++) {
            ProductParamItemVO item = productParams.get(i);
            System.out.println("参数 " + i + ": paramId=" + item.getParamId() + ", key=" + item.getKey() + ", value=" + item.getValue());

            if (item.getParamId() == null) {
                System.out.println("参数ID为空，跳过");
                continue;
            }

            PmsProductParamDO paramDO = PmsProductParamDO.builder()
                    .productId(productId)
                    .paramId(item.getParamId())
                    .sort(i)
                    .build();
            pmsProductParamMapper.insert(paramDO);
            System.out.println("参数保存成功: " + paramDO);
        }
    }

    /**
     * 保存SKU库存列表
     */
    private void saveSkuStockList(Long productId, List<PmsProductCreateReqVO.SkuStockItemVO> skuStockList) {
        if (CollectionUtils.isEmpty(skuStockList)) {
            log.warn("SKU库存列表为空，跳过保存");
            return;
        }

        log.info("开始保存SKU库存，商品ID: {}, SKU数量: {}", productId, skuStockList.size());

        for (PmsProductCreateReqVO.SkuStockItemVO skuItem : skuStockList) {
            // 序列化规格数据为JSON
            String spDataJson = null;
            if (!CollectionUtils.isEmpty(skuItem.getSpecs())) {
                try {
                    // 构建JSON格式: [{"key":"规格名","value":"规格值"}]
                    StringBuilder jsonBuilder = new StringBuilder("[");
                    for (int i = 0; i < skuItem.getSpecs().size(); i++) {
                        PmsProductCreateReqVO.SkuSpecItemVO spec = skuItem.getSpecs().get(i);
                        if (i > 0) jsonBuilder.append(",");
                        jsonBuilder.append("{\"key\":\"").append(spec.getSpecKey())
                                .append("\",\"value\":\"").append(spec.getSpecValue()).append("\"}");
                    }
                    jsonBuilder.append("]");
                    spDataJson = jsonBuilder.toString();
                } catch (Exception e) {
                    log.error("序列化SKU规格数据失败", e);
                }
            }

            PmsSkuStockDO skuStockDO = PmsSkuStockDO.builder()
                    .productId(productId)
                    .skuCode(skuItem.getSkuCode())
                    .price(skuItem.getPrice())
                    .stock(skuItem.getStock())
                    .promotionPrice(skuItem.getPromotionPrice())
                    .lowStock(skuItem.getLowStock() != null ? skuItem.getLowStock() : 0)
                    .lockStock(0)
                    .pic(skuItem.getPic())
                    .sale(0)
                    .spData(spDataJson)
                    .build();

            pmsSkuStockMapper.insert(skuStockDO);
            log.info("SKU保存成功: skuCode={}, price={}, stock={}", skuItem.getSkuCode(), skuItem.getPrice(), skuItem.getStock());
        }
    }
}