package com.gg.guimall.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.admin.model.vo.oms.*;
import com.gg.guimall.admin.model.vo.oms.FindOmsOrderPageListReqVO;
import com.gg.guimall.admin.model.vo.oms.FindOmsOrderPageListRspVO;
import com.gg.guimall.admin.service.OmsOrderService;
import com.gg.guimall.common.domain.dos.OmsOrderDO;
import com.gg.guimall.common.domain.dos.OmsOrderItemDO;
import com.gg.guimall.common.domain.mapper.OmsOrderItemMapper;
import com.gg.guimall.common.domain.mapper.OmsOrderMapper;
import com.gg.guimall.common.enums.ResponseCodeEnum;
import com.gg.guimall.common.exception.BizException;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 * @description: 订单管理 Service 实现类
 **/
@Service("adminOmsOrderServiceImpl")
@Slf4j
public class OmsOrderServiceImpl implements OmsOrderService {

    @Autowired
    private OmsOrderMapper orderMapper;

    @Autowired
    private OmsOrderItemMapper orderItemMapper;

    @Override
    public PageResponse<FindOmsOrderPageListRspVO> findOrderPageList(FindOmsOrderPageListReqVO reqVO) {

        Page<OmsOrderDO> page = orderMapper.selectPageList(
                reqVO.getCurrent(),
                reqVO.getSize(),
                reqVO.getOrderId(),
                reqVO.getOrderSn(),
                reqVO.getMemberId(),
                reqVO.getMemberUsername(),
                reqVO.getFarmerId(),
                reqVO.getStatus(),
                null,
                null
        );

        List<FindOmsOrderPageListRspVO> voList = page.getRecords().stream()
                .map(item -> {
                    FindOmsOrderPageListRspVO vo = new FindOmsOrderPageListRspVO();
                    BeanUtils.copyProperties(item, vo);
                    return vo;
                })
                .collect(Collectors.toList());

        return PageResponse.success(page, voList);
    }

    @Override
    public Response findOrderDetail(Long id) {
        if (Objects.isNull(id) || id <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        OmsOrderDO orderDO = orderMapper.selectById(id);
        if (Objects.isNull(orderDO) || Objects.equals(orderDO.getDeleteStatus(), 1)) {
            throw new BizException(ResponseCodeEnum.ORDER_NOT_FOUND);
        }

        List<OmsOrderItemDO> itemDOList = orderItemMapper.selectByOrderId(id);
        List<FindOmsOrderDetailRspVO.OrderItem> items = itemDOList.stream()
                .map(itemDO -> FindOmsOrderDetailRspVO.OrderItem.builder()
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

    @Override
    public Response deliverOrder(DeliverOmsOrderReqVO reqVO) {
        OmsOrderDO orderDO = getValidOrder(reqVO.getId());
        // 仅“待发货(1)”允许发货 -> “已发货(2)”
        if (!Objects.equals(orderDO.getStatus(), 1)) {
            throw new BizException(ResponseCodeEnum.ORDER_STATUS_ILLEGAL);
        }

        OmsOrderDO updateDO = OmsOrderDO.builder()
                .id(orderDO.getId())
                .status(2)
                .deliveryCompany(reqVO.getDeliveryCompany())
                .deliverySn(reqVO.getDeliverySn())
                .deliveryTime(LocalDateTime.now())
                .build();
        orderMapper.updateById(updateDO);
        return Response.success();
    }

    @Override
    public Response closeOrder(CloseOmsOrderReqVO reqVO) {
        OmsOrderDO orderDO = getValidOrder(reqVO.getId());
        // 允许关闭：待付款(0)、待发货(1)
        if (!Objects.equals(orderDO.getStatus(), 0) && !Objects.equals(orderDO.getStatus(), 1)) {
            throw new BizException(ResponseCodeEnum.ORDER_STATUS_ILLEGAL);
        }

        OmsOrderDO updateDO = OmsOrderDO.builder()
                .id(orderDO.getId())
                .status(4)
                .note(reqVO.getNote())
                .build();
        orderMapper.updateById(updateDO);
        return Response.success();
    }

    @Override
    public Response remarkOrder(RemarkOmsOrderReqVO reqVO) {
        OmsOrderDO orderDO = getValidOrder(reqVO.getId());
        // 已关闭(4)/无效(5)不允许修改备注（可按需放开）
        if (Objects.equals(orderDO.getStatus(), 4) || Objects.equals(orderDO.getStatus(), 5)) {
            throw new BizException(ResponseCodeEnum.ORDER_STATUS_ILLEGAL);
        }

        OmsOrderDO updateDO = OmsOrderDO.builder()
                .id(orderDO.getId())
                .note(reqVO.getNote())
                .build();
        orderMapper.updateById(updateDO);
        return Response.success();
    }

    private OmsOrderDO getValidOrder(Long id) {
        if (Objects.isNull(id) || id <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }
        OmsOrderDO orderDO = orderMapper.selectById(id);
        if (Objects.isNull(orderDO) || Objects.equals(orderDO.getDeleteStatus(), 1)) {
            throw new BizException(ResponseCodeEnum.ORDER_NOT_FOUND);
        }
        return orderDO;
    }
}

