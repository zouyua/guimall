package com.gg.guimall.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.admin.model.vo.pms.*;
import com.gg.guimall.admin.service.PmsProductService;
import com.gg.guimall.common.domain.dos.PmsProductDO;
import com.gg.guimall.common.domain.mapper.PmsProductMapper;
import com.gg.guimall.common.enums.ResponseCodeEnum;
import com.gg.guimall.common.exception.BizException;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 商品管理 Service 实现类
 *
 * 负责商品的增删改查
 *
 * @author wly
 */
@Service
@Slf4j
public class PmsProductServiceImpl implements PmsProductService {

    @Autowired
    private PmsProductMapper pmsProductMapper;

    /**
     * 创建商品
     */
    @Override
    public Response createProduct(PmsProductCreateReqVO reqVO) {

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
                .originalPrice(reqVO.getOriginalPrice())
                .stock(reqVO.getStock())
                .unit(reqVO.getUnit())
                .weight(reqVO.getWeight())
                .keywords(reqVO.getKeywords())
                .note(reqVO.getNote())
                .detailTitle(reqVO.getDetailTitle())
                .detailDesc(reqVO.getDetailDesc())
                .detailHtml(reqVO.getDetailHtml())
                .publishStatus(reqVO.getPublishStatus())
                .sort(reqVO.getSort())
                .build();

        // 插入数据库
        pmsProductMapper.insert(productDO);

        return Response.success();
    }

    /**
     * 商品分页查询
     */
    @Override
    public PageResponse findProductPageList(FindPmsProductPageListReqVO reqVO) {

        // 分页查询
        Page<PmsProductDO> page = pmsProductMapper.selectPageList(
                reqVO.getCurrent(),
                reqVO.getSize(),
                reqVO.getName(),
                reqVO.getCategoryId(),
                reqVO.getPublishStatus()
        );

        return PageResponse.success(page);
    }

    /**
     * 查询商品详情
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

        return Response.success(rspVO);
    }

    /**
     * 修改商品
     */
    @Override
    public Response updateProduct(PmsProductUpdateReqVO reqVO) {

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
                .originalPrice(reqVO.getOriginalPrice())
                .stock(reqVO.getStock())
                .unit(reqVO.getUnit())
                .weight(reqVO.getWeight())
                .keywords(reqVO.getKeywords())
                .note(reqVO.getNote())
                .detailTitle(reqVO.getDetailTitle())
                .detailDesc(reqVO.getDetailDesc())
                .detailHtml(reqVO.getDetailHtml())
                .publishStatus(reqVO.getPublishStatus())
                .sort(reqVO.getSort())
                .build();

        pmsProductMapper.updateById(productDO);

        return Response.success();
    }

    /**
     * 删除商品
     */
    @Override
    public Response deleteProduct(Long id) {

        pmsProductMapper.deleteById(id);

        return Response.success();
    }
}