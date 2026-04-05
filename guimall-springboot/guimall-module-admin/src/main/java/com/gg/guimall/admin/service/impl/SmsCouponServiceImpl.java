package com.gg.guimall.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.admin.model.vo.sms.CreateSmsCouponReqVO;
import com.gg.guimall.admin.model.vo.sms.FindSmsCouponDetailRspVO;
import com.gg.guimall.admin.model.vo.sms.FindSmsCouponPageListReqVO;
import com.gg.guimall.admin.model.vo.sms.FindSmsCouponPageListRspVO;
import com.gg.guimall.admin.model.vo.sms.UpdateSmsCouponReqVO;
import com.gg.guimall.admin.service.SmsCouponService;
import com.gg.guimall.common.domain.dos.PmsProductCategoryDO;
import com.gg.guimall.common.domain.dos.PmsProductDO;
import com.gg.guimall.common.domain.dos.SmsCouponDO;
import com.gg.guimall.common.domain.dos.SmsCouponProductCategoryRelationDO;
import com.gg.guimall.common.domain.dos.SmsCouponProductRelationDO;
import com.gg.guimall.common.domain.mapper.PmsProductCategoryMapper;
import com.gg.guimall.common.domain.mapper.PmsProductMapper;
import com.gg.guimall.common.domain.mapper.SmsCouponMapper;
import com.gg.guimall.common.domain.mapper.SmsCouponProductCategoryRelationMapper;
import com.gg.guimall.common.domain.mapper.SmsCouponProductRelationMapper;
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

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 优惠券管理 Service 实现类
 *
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 */
@Service
@Slf4j
public class SmsCouponServiceImpl implements SmsCouponService {

    @Autowired
    private SmsCouponMapper couponMapper;

    @Autowired
    private SmsCouponProductRelationMapper couponProductRelationMapper;

    @Autowired
    private SmsCouponProductCategoryRelationMapper couponProductCategoryRelationMapper;

    @Autowired
    private PmsProductMapper productMapper;

    @Autowired
    private PmsProductCategoryMapper productCategoryMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response createCoupon(CreateSmsCouponReqVO reqVO) {
        if (Objects.isNull(reqVO.getType()) ||
                Objects.isNull(reqVO.getName()) ||
                Objects.isNull(reqVO.getPlatform()) ||
                Objects.isNull(reqVO.getCount()) ||
                Objects.isNull(reqVO.getAmount()) ||
                Objects.isNull(reqVO.getPerLimit()) ||
                Objects.isNull(reqVO.getMinAmount()) ||
                Objects.isNull(reqVO.getStartTime()) ||
                Objects.isNull(reqVO.getEndTime()) ||
                Objects.isNull(reqVO.getUseType())) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        SmsCouponDO couponDO = SmsCouponDO.builder()
                .type(reqVO.getType())
                .name(reqVO.getName())
                .platform(reqVO.getPlatform())
                .totalCount(reqVO.getCount())
                .publishCount(reqVO.getCount())  // 发行数量等于总数量
                .amount(reqVO.getAmount())
                .perLimit(reqVO.getPerLimit())
                .minAmount(reqVO.getMinAmount())
                .startTime(reqVO.getStartTime())
                .endTime(reqVO.getEndTime())
                .useType(reqVO.getUseType())
                .note(reqVO.getNote())
                .enableTime(reqVO.getEnableTime())
                .code(reqVO.getCode())
                .memberLevel(reqVO.getMemberLevel())
                .build();

        couponMapper.insert(couponDO);
        Long couponId = couponDO.getId();

        // 处理关联关系
        // useType: 0全场 1指定分类 2指定商品
        if (reqVO.getUseType() == 1 && !CollectionUtils.isEmpty(reqVO.getProductCategoryIds())) {
            // 指定分类：插入 sms_coupon_product_category_relation
            for (Long categoryId : reqVO.getProductCategoryIds()) {
                PmsProductCategoryDO category = productCategoryMapper.selectById(categoryId);
                if (category != null) {
                    SmsCouponProductCategoryRelationDO relation = SmsCouponProductCategoryRelationDO.builder()
                            .couponId(couponId)
                            .productCategoryId(categoryId)
                            .productCategoryName(category.getName())
                            .parentCategoryName(category.getParentId() != null && category.getParentId() > 0 ?
                                    getParentCategoryName(category.getParentId()) : "")
                            .build();
                    couponProductCategoryRelationMapper.insert(relation);
                }
            }
        } else if (reqVO.getUseType() == 2 && !CollectionUtils.isEmpty(reqVO.getProductIds())) {
            // 指定商品：插入 sms_coupon_product_relation
            for (Long productId : reqVO.getProductIds()) {
                PmsProductDO product = productMapper.selectById(productId);
                if (product != null) {
                    SmsCouponProductRelationDO relation = SmsCouponProductRelationDO.builder()
                            .couponId(couponId)
                            .productId(productId)
                            .productName(product.getName())
                            .productSn(product.getProductSn())
                            .build();
                    couponProductRelationMapper.insert(relation);
                }
            }
        }

        return Response.success();
    }

    private String getParentCategoryName(Long parentId) {
        PmsProductCategoryDO parent = productCategoryMapper.selectById(parentId);
        return parent != null ? parent.getName() : "";
    }

    @Override
    public PageResponse findCouponPageList(FindSmsCouponPageListReqVO reqVO) {
        Page<SmsCouponDO> page = couponMapper.selectPageList(
                reqVO.getCurrent(),
                reqVO.getSize(),
                reqVO.getName(),
                reqVO.getType(),
                reqVO.getUseType(),
                reqVO.getPlatform(),
                reqVO.getBeginTime(),
                reqVO.getEndTime()
        );

        List<FindSmsCouponPageListRspVO> voList = page.getRecords().stream()
                .map(item -> {
                    FindSmsCouponPageListRspVO vo = new FindSmsCouponPageListRspVO();
                    BeanUtils.copyProperties(item, vo);
                    vo.setCount(item.getTotalCount());
                    return vo;
                })
                .collect(Collectors.toList());

        return PageResponse.success(page, voList);
    }

    @Override
    public Response findCouponDetail(Long id) {
        if (Objects.isNull(id) || id <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        SmsCouponDO couponDO = couponMapper.selectById(id);
        if (Objects.isNull(couponDO)) {
            throw new BizException(ResponseCodeEnum.COUPON_NOT_FOUND);
        }

        FindSmsCouponDetailRspVO rspVO = new FindSmsCouponDetailRspVO();
        BeanUtils.copyProperties(couponDO, rspVO);
        rspVO.setCount(couponDO.getTotalCount());
        return Response.success(rspVO);
    }

    @Override
    public Response updateCoupon(UpdateSmsCouponReqVO reqVO) {
        if (Objects.isNull(reqVO.getId()) || reqVO.getId() <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        SmsCouponDO exist = couponMapper.selectById(reqVO.getId());
        if (Objects.isNull(exist)) {
            throw new BizException(ResponseCodeEnum.COUPON_NOT_FOUND);
        }

        SmsCouponDO updateDO = SmsCouponDO.builder()
                .id(reqVO.getId())
                .type(reqVO.getType())
                .name(reqVO.getName())
                .platform(reqVO.getPlatform())
                .totalCount(reqVO.getCount())
                .amount(reqVO.getAmount())
                .perLimit(reqVO.getPerLimit())
                .minAmount(reqVO.getMinAmount())
                .startTime(reqVO.getStartTime())
                .endTime(reqVO.getEndTime())
                .useType(reqVO.getUseType())
                .note(reqVO.getNote())
                .enableTime(reqVO.getEnableTime())
                .code(reqVO.getCode())
                .memberLevel(reqVO.getMemberLevel())
                .build();

        couponMapper.updateById(updateDO);
        return Response.success();
    }

    @Override
    public Response deleteCoupon(Long id) {
        if (Objects.isNull(id) || id <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        SmsCouponDO exist = couponMapper.selectById(id);
        if (Objects.isNull(exist)) {
            throw new BizException(ResponseCodeEnum.COUPON_NOT_FOUND);
        }

        couponMapper.deleteById(id);
        return Response.success();
    }
}

