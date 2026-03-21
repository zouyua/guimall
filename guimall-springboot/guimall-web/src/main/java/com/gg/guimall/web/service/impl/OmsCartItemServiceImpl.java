package com.gg.guimall.web.service.impl;

import com.gg.guimall.common.domain.dos.OmsCartItemDO;
import com.gg.guimall.common.domain.dos.PmsProductDO;
import com.gg.guimall.common.domain.dos.PmsSkuStockDO;
import com.gg.guimall.common.domain.mapper.OmsCartItemMapper;
import com.gg.guimall.common.domain.mapper.PmsProductMapper;
import com.gg.guimall.common.domain.mapper.PmsSkuStockMapper;
import com.gg.guimall.common.enums.ResponseCodeEnum;
import com.gg.guimall.common.exception.BizException;
import com.gg.guimall.common.utils.Response;
import com.gg.guimall.web.model.vo.cart.*;
import com.gg.guimall.web.service.OmsCartItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 购物车 Service 实现类
 */
@Service
@Slf4j
public class OmsCartItemServiceImpl implements OmsCartItemService {

    @Autowired
    private OmsCartItemMapper cartItemMapper;

    @Autowired
    private PmsProductMapper productMapper;

    @Autowired
    private PmsSkuStockMapper skuStockMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response add(AddCartItemReqVO reqVO) {
        if (reqVO.getQuantity() == null || reqVO.getQuantity() <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        PmsProductDO productDO = productMapper.selectById(reqVO.getProductId());
        if (productDO == null || !Objects.equals(productDO.getDeleteStatus(), 0)) {
            throw new BizException(ResponseCodeEnum.PRODUCT_NOT_FOUND);
        }

        PmsSkuStockDO skuDO = skuStockMapper.selectById(reqVO.getProductSkuId());
        if (skuDO == null || !Objects.equals(skuDO.getProductId(), reqVO.getProductId())) {
            throw new BizException(ResponseCodeEnum.SKU_NOT_FOUND);
        }

        BigDecimal price = skuDO.getPromotionPrice() != null ? skuDO.getPromotionPrice()
                : (skuDO.getPrice() != null ? skuDO.getPrice() : productDO.getPrice());

        OmsCartItemDO exist = cartItemMapper.selectOneByMemberSkuAndAttr(
                reqVO.getMemberId(),
                reqVO.getProductSkuId(),
                reqVO.getProductAttr()
        );

        if (exist != null) {
            OmsCartItemDO updateDO = OmsCartItemDO.builder()
                    .id(exist.getId())
                    .quantity((exist.getQuantity() == null ? 0 : exist.getQuantity()) + reqVO.getQuantity())
                    .price(price)
                    .updateTime(LocalDateTime.now())
                    .build();
            cartItemMapper.updateById(updateDO);
            return Response.success();
        }

        OmsCartItemDO cartItemDO = OmsCartItemDO.builder()
                .memberId(reqVO.getMemberId())
                .productId(productDO.getId())
                .productSkuId(skuDO.getId())
                .quantity(reqVO.getQuantity())
                .price(price)
                .productPic(Objects.nonNull(skuDO.getPic()) ? skuDO.getPic() : productDO.getPic())
                .productName(productDO.getName())
                .productSubTitle(productDO.getSubTitle())
                .productSkuCode(skuDO.getSkuCode())
                .productCategoryId(productDO.getProductCategoryId())
                .productSn(productDO.getProductSn())
                .productAttr(reqVO.getProductAttr())
                .deleteStatus(0)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        cartItemMapper.insert(cartItemDO);
        return Response.success();
    }

    @Override
    public Response clear(ClearCartReqVO reqVO) {
        cartItemMapper.clearByMemberId(reqVO.getMemberId());
        return Response.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response delete(DeleteCartItemReqVO reqVO) {
        if (reqVO.getIds() == null || reqVO.getIds().isEmpty()) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }
        // 逻辑删除：利用 @TableLogic + deleteById
        reqVO.getIds().forEach(id -> {
            OmsCartItemDO exist = cartItemMapper.selectById(id);
            if (exist != null && Objects.equals(exist.getMemberId(), reqVO.getMemberId())) {
                cartItemMapper.deleteById(id);
            }
        });
        return Response.success();
    }

    @Override
    public Response list(Long memberId) {
        List<OmsCartItemDO> list = cartItemMapper.selectByMemberId(memberId);
        return Response.success(list);
    }

    @Override
    public Response getProduct(Long productId) {
        // 返回商品 SKU 列表，供前端选择规格
        List<PmsSkuStockDO> skuList = skuStockMapper.selectByProductId(productId);
        return Response.success(skuList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response updateAttr(UpdateCartAttrReqVO reqVO) {
        OmsCartItemDO cartItemDO = cartItemMapper.selectById(reqVO.getId());
        if (cartItemDO == null || !Objects.equals(cartItemDO.getMemberId(), reqVO.getMemberId())) {
            throw new BizException(ResponseCodeEnum.CART_ITEM_NOT_FOUND);
        }

        PmsSkuStockDO skuDO = skuStockMapper.selectById(reqVO.getProductSkuId());
        if (skuDO == null || !Objects.equals(skuDO.getProductId(), reqVO.getProductId())) {
            throw new BizException(ResponseCodeEnum.SKU_NOT_FOUND);
        }

        PmsProductDO productDO = productMapper.selectById(reqVO.getProductId());
        if (productDO == null || !Objects.equals(productDO.getDeleteStatus(), 0)) {
            throw new BizException(ResponseCodeEnum.PRODUCT_NOT_FOUND);
        }

        BigDecimal price = skuDO.getPromotionPrice() != null ? skuDO.getPromotionPrice()
                : (skuDO.getPrice() != null ? skuDO.getPrice() : productDO.getPrice());

        OmsCartItemDO updateDO = OmsCartItemDO.builder()
                .id(cartItemDO.getId())
                .productSkuId(skuDO.getId())
                .productSkuCode(skuDO.getSkuCode())
                .productPic(Objects.nonNull(skuDO.getPic()) ? skuDO.getPic() : productDO.getPic())
                .price(price)
                .productAttr(reqVO.getProductAttr())
                .updateTime(LocalDateTime.now())
                .build();
        cartItemMapper.updateById(updateDO);
        return Response.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response updateQuantity(Long memberId, Long id, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }
        OmsCartItemDO cartItemDO = cartItemMapper.selectById(id);
        if (cartItemDO == null || !Objects.equals(cartItemDO.getMemberId(), memberId)) {
            throw new BizException(ResponseCodeEnum.CART_ITEM_NOT_FOUND);
        }
        OmsCartItemDO updateDO = OmsCartItemDO.builder()
                .id(cartItemDO.getId())
                .quantity(quantity)
                .updateTime(LocalDateTime.now())
                .build();
        cartItemMapper.updateById(updateDO);
        return Response.success();
    }
}

