package com.gg.guimall.web.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.common.domain.dos.OmsCartItemDO;
import com.gg.guimall.common.domain.dos.OmsOrderDO;
import com.gg.guimall.common.domain.dos.OmsOrderItemDO;
import com.gg.guimall.common.domain.dos.PmsProductDO;
import com.gg.guimall.common.domain.mapper.OmsCartItemMapper;
import com.gg.guimall.common.domain.mapper.OmsOrderItemMapper;
import com.gg.guimall.common.domain.mapper.OmsOrderMapper;
import com.gg.guimall.common.domain.mapper.PmsProductMapper;
import com.gg.guimall.common.enums.ResponseCodeEnum;
import com.gg.guimall.common.exception.BizException;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;
import com.gg.guimall.web.model.vo.oms.FindOmsOrderDetailReqVO;
import com.gg.guimall.web.model.vo.oms.FindOmsOrderDetailRspVO;
import com.gg.guimall.web.model.vo.oms.FindOmsOrderPageListReqVO;
import com.gg.guimall.web.model.vo.oms.FindOmsOrderPageListRspVO;
import com.gg.guimall.web.model.vo.oms.FindOmsOrderDetailRspVO.OrderItem;
import com.gg.guimall.web.model.vo.oms.SubmitOmsOrderReqVO;
import com.gg.guimall.web.model.vo.oms.SubmitOmsOrderRspVO;
import com.gg.guimall.web.service.OmsOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 前台订单 Service 实现类
 */
@Service("webOmsOrderServiceImpl")
@Slf4j
public class OmsOrderServiceImpl implements OmsOrderService {

    @Autowired
    private OmsOrderMapper orderMapper;

    @Autowired
    private OmsOrderItemMapper orderItemMapper;

    @Autowired
    private OmsCartItemMapper cartItemMapper;

    @Autowired
    private PmsProductMapper pmsProductMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response submitOrder(SubmitOmsOrderReqVO reqVO) {
        if (Objects.isNull(reqVO) || Objects.isNull(reqVO.getMemberId()) || reqVO.getMemberId() <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        List<OmsCartItemDO> cartItems = cartItemMapper.selectByMemberId(reqVO.getMemberId());
        if (Objects.isNull(cartItems) || cartItems.isEmpty()) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        LocalDateTime now = LocalDateTime.now();

        BigDecimal totalAmount = BigDecimal.ZERO;
        Long farmerId = null;

        for (OmsCartItemDO item : cartItems) {
            if (Objects.isNull(item) || Objects.isNull(item.getQuantity()) || item.getQuantity() <= 0) {
                continue;
            }
            BigDecimal price = Objects.isNull(item.getPrice()) ? BigDecimal.ZERO : item.getPrice();
            BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
            totalAmount = totalAmount.add(price.multiply(quantity));

            if (Objects.isNull(farmerId) && Objects.nonNull(item.getProductId())) {
                PmsProductDO productDO = pmsProductMapper.selectById(item.getProductId());
                if (Objects.nonNull(productDO)) {
                    farmerId = productDO.getFarmerId();
                }
            }
        }

        if (totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        String orderSn = "SN" + System.currentTimeMillis() + "_" + reqVO.getMemberId();

        OmsOrderDO orderDO = OmsOrderDO.builder()
                .memberId(reqVO.getMemberId())
                .farmerId(farmerId)
                .couponId(null)
                .orderSn(orderSn)
                .createTime(now)
                .updateTime(now)
                .memberUsername(null)
                .totalAmount(totalAmount)
                .payAmount(totalAmount)
                .freightAmount(BigDecimal.ZERO)
                .promotionAmount(BigDecimal.ZERO)
                .integrationAmount(BigDecimal.ZERO)
                .couponAmount(BigDecimal.ZERO)
                .discountAmount(BigDecimal.ZERO)
                .payType(0)
                .sourceType(0)
                .status(0)
                .orderType(0)
                .autoConfirmDay(null)
                .integration(0)
                .growth(0)
                .promotionInfo(null)
                .billType(null)
                .billHeader(null)
                .billContent(null)
                .receiverName(reqVO.getReceiverName())
                .receiverPhone(reqVO.getReceiverPhone())
                .receiverPostCode(reqVO.getReceiverPostCode())
                .receiverProvince(reqVO.getReceiverProvince())
                .receiverCity(reqVO.getReceiverCity())
                .receiverRegion(reqVO.getReceiverRegion())
                .receiverDetailAddress(reqVO.getReceiverDetailAddress())
                .note(reqVO.getNote())
                .confirmStatus(0)
                .deleteStatus(0)
                .build();

        orderMapper.insert(orderDO);
        Long orderId = orderDO.getId();
        if (Objects.isNull(orderId) || orderId <= 0) {
            throw new BizException(ResponseCodeEnum.SYSTEM_ERROR);
        }

        BigDecimal realAmount;
        for (OmsCartItemDO item : cartItems) {
            if (Objects.isNull(item) || Objects.isNull(item.getQuantity()) || item.getQuantity() <= 0) {
                continue;
            }

            BigDecimal price = Objects.isNull(item.getPrice()) ? BigDecimal.ZERO : item.getPrice();
            realAmount = price.multiply(BigDecimal.valueOf(item.getQuantity()));

            OmsOrderItemDO orderItemDO = OmsOrderItemDO.builder()
                    .orderId(orderId)
                    .orderSn(orderSn)
                    .productId(item.getProductId())
                    .productPic(item.getProductPic())
                    .productName(item.getProductName())
                    .productSn(item.getProductSn())
                    .productPrice(price)
                    .productQuantity(item.getQuantity())
                    .productSkuId(item.getProductSkuId())
                    .productSkuCode(item.getProductSkuCode())
                    .productCategoryId(item.getProductCategoryId())
                    .promotionName(null)
                    .promotionAmount(BigDecimal.ZERO)
                    .couponAmount(BigDecimal.ZERO)
                    .integrationAmount(BigDecimal.ZERO)
                    .realAmount(realAmount)
                    .giftIntegration(0)
                    .giftGrowth(0)
                    .productAttr(item.getProductAttr())
                    .build();

            orderItemMapper.insert(orderItemDO);
        }

        // 提交成功：清空购物车
        cartItemMapper.clearByMemberId(reqVO.getMemberId());

        SubmitOmsOrderRspVO rspVO = new SubmitOmsOrderRspVO();
        rspVO.setOrderId(orderId);
        rspVO.setOrderSn(orderSn);
        rspVO.setTotalAmount(totalAmount);
        rspVO.setPayAmount(totalAmount);
        rspVO.setCreateTime(now);
        return Response.success(rspVO);
    }

    @Override
    public PageResponse<FindOmsOrderPageListRspVO> findOrderPageList(FindOmsOrderPageListReqVO reqVO) {
        if (Objects.isNull(reqVO) || Objects.isNull(reqVO.getMemberId()) || reqVO.getMemberId() <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        Page<OmsOrderDO> page = orderMapper.selectPageList(
                reqVO.getCurrent(),
                reqVO.getSize(),
                null,
                reqVO.getOrderSn(),
                reqVO.getMemberId(),
                null,
                null,
                reqVO.getStatus(),
                null,
                null
        );

        List<FindOmsOrderPageListRspVO> voList = page.getRecords().stream()
                .map(order -> {
                    FindOmsOrderPageListRspVO vo = new FindOmsOrderPageListRspVO();
                    BeanUtils.copyProperties(order, vo);
                    return vo;
                })
                .collect(Collectors.toList());

        return PageResponse.success(page, voList);
    }

    @Override
    public Response findOrderDetail(FindOmsOrderDetailReqVO reqVO) {
        if (Objects.isNull(reqVO) || Objects.isNull(reqVO.getId()) || reqVO.getId() <= 0
                || Objects.isNull(reqVO.getMemberId()) || reqVO.getMemberId() <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        OmsOrderDO orderDO = orderMapper.selectById(reqVO.getId());
        if (Objects.isNull(orderDO) || Objects.equals(orderDO.getDeleteStatus(), 1)
                || !Objects.equals(orderDO.getMemberId(), reqVO.getMemberId())) {
            throw new BizException(ResponseCodeEnum.ORDER_NOT_FOUND);
        }

        List<com.gg.guimall.common.domain.dos.OmsOrderItemDO> itemDOList = orderItemMapper.selectByOrderId(reqVO.getId());
        List<OrderItem> items = itemDOList.stream()
                .map(itemDO -> OrderItem.builder()
                        .id(itemDO.getId())
                        .productId(itemDO.getProductId())
                        .productName(itemDO.getProductName())
                        .productPic(itemDO.getProductPic())
                        .productSkuId(itemDO.getProductSkuId())
                        .productSkuCode(itemDO.getProductSkuCode())
                        .productPrice(itemDO.getProductPrice())
                        .productQuantity(itemDO.getProductQuantity())
                        .realAmount(itemDO.getRealAmount())
                        .productAttr(itemDO.getProductAttr())
                        .build())
                .collect(Collectors.toList());

        FindOmsOrderDetailRspVO rspVO = new FindOmsOrderDetailRspVO();
        BeanUtils.copyProperties(orderDO, rspVO);
        rspVO.setItems(items);
        return Response.success(rspVO);
    }
}

