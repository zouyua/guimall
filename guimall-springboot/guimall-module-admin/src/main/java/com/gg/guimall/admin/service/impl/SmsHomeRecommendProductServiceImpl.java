package com.gg.guimall.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.admin.model.vo.sms.CreateSmsHomeRecommendProductReqVO;
import com.gg.guimall.admin.model.vo.sms.FindSmsHomeRecommendProductDetailRspVO;
import com.gg.guimall.admin.model.vo.sms.FindSmsHomeRecommendProductPageListReqVO;
import com.gg.guimall.admin.model.vo.sms.FindSmsHomeRecommendProductPageListRspVO;
import com.gg.guimall.admin.model.vo.sms.HomeRecommendProductOptionVO;
import com.gg.guimall.admin.model.vo.sms.UpdateSmsHomeRecommendProductReqVO;
import com.gg.guimall.admin.service.SmsHomeRecommendProductService;
import com.gg.guimall.common.domain.dos.PmsProductDO;
import com.gg.guimall.common.domain.dos.SmsHomeRecommendProductDO;
import com.gg.guimall.common.domain.mapper.PmsProductMapper;
import com.gg.guimall.common.domain.mapper.SmsHomeRecommendProductMapper;
import com.gg.guimall.common.enums.ResponseCodeEnum;
import com.gg.guimall.common.exception.BizException;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 人气推荐管理 Service 实现类
 */
@Service
@Slf4j
public class SmsHomeRecommendProductServiceImpl implements SmsHomeRecommendProductService {

    @Autowired
    private SmsHomeRecommendProductMapper smsHomeRecommendProductMapper;

    @Autowired
    private PmsProductMapper pmsProductMapper;

    @Override
    public Response createRecommendProduct(CreateSmsHomeRecommendProductReqVO reqVO) {
        if (Objects.isNull(reqVO.getProductId()) ||
                Objects.isNull(reqVO.getProductName()) ||
                Objects.isNull(reqVO.getRecommendStatus()) ||
                Objects.isNull(reqVO.getSort())) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }
        if (reqVO.getProductId() <= 0 || reqVO.getSort() < 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }
        if (!Objects.equals(reqVO.getRecommendStatus(), 0) && !Objects.equals(reqVO.getRecommendStatus(), 1)) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        PmsProductDO productDO = pmsProductMapper.selectById(reqVO.getProductId());
        if (Objects.isNull(productDO) ||
                !Objects.equals(productDO.getIsDeleted(), 0) ||
                !Objects.equals(productDO.getPublishStatus(), 1)) {
            throw new BizException(ResponseCodeEnum.PRODUCT_NOT_FOUND);
        }

        // 以商品表为准，避免 productName 不一致
        SmsHomeRecommendProductDO homeDO = SmsHomeRecommendProductDO.builder()
                .productId(reqVO.getProductId())
                .productName(productDO.getName())
                .recommendStatus(reqVO.getRecommendStatus())
                .sort(reqVO.getSort())
                .build();

        smsHomeRecommendProductMapper.insert(homeDO);
        return Response.success();
    }

    @Override
    public PageResponse<FindSmsHomeRecommendProductPageListRspVO> findRecommendProductPageList(FindSmsHomeRecommendProductPageListReqVO reqVO) {
        Page<SmsHomeRecommendProductDO> page = smsHomeRecommendProductMapper.selectPageList(
                reqVO.getCurrent(),
                reqVO.getSize(),
                reqVO.getProductName(),
                reqVO.getRecommendStatus()
        );

        List<FindSmsHomeRecommendProductPageListRspVO> voList = page.getRecords().stream()
                .map(item -> {
                    FindSmsHomeRecommendProductPageListRspVO vo = new FindSmsHomeRecommendProductPageListRspVO();
                    BeanUtils.copyProperties(item, vo);
                    return vo;
                })
                .collect(Collectors.toList());

        return PageResponse.success(page, voList);
    }

    @Override
    public Response findRecommendProductDetail(Long id) {
        if (Objects.isNull(id) || id <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        SmsHomeRecommendProductDO homeDO = smsHomeRecommendProductMapper.selectById(id);
        if (Objects.isNull(homeDO)) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        FindSmsHomeRecommendProductDetailRspVO rspVO = new FindSmsHomeRecommendProductDetailRspVO();
        BeanUtils.copyProperties(homeDO, rspVO);
        return Response.success(rspVO);
    }

    @Override
    public Response updateRecommendProduct(UpdateSmsHomeRecommendProductReqVO reqVO) {
        if (Objects.isNull(reqVO.getId()) ||
                Objects.isNull(reqVO.getProductId()) ||
                Objects.isNull(reqVO.getProductName()) ||
                Objects.isNull(reqVO.getRecommendStatus()) ||
                Objects.isNull(reqVO.getSort())) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }
        if (reqVO.getId() <= 0 || reqVO.getProductId() <= 0 || reqVO.getSort() < 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }
        if (!Objects.equals(reqVO.getRecommendStatus(), 0) && !Objects.equals(reqVO.getRecommendStatus(), 1)) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        SmsHomeRecommendProductDO exist = smsHomeRecommendProductMapper.selectById(reqVO.getId());
        if (Objects.isNull(exist)) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        PmsProductDO productDO = pmsProductMapper.selectById(reqVO.getProductId());
        if (Objects.isNull(productDO) ||
                !Objects.equals(productDO.getIsDeleted(), 0) ||
                !Objects.equals(productDO.getPublishStatus(), 1)) {
            throw new BizException(ResponseCodeEnum.PRODUCT_NOT_FOUND);
        }

        SmsHomeRecommendProductDO updateDO = SmsHomeRecommendProductDO.builder()
                .id(reqVO.getId())
                .productId(reqVO.getProductId())
                .productName(productDO.getName())
                .recommendStatus(reqVO.getRecommendStatus())
                .sort(reqVO.getSort())
                .build();

        smsHomeRecommendProductMapper.updateById(updateDO);
        return Response.success();
    }

    @Override
    public Response deleteRecommendProduct(Long id) {
        if (Objects.isNull(id) || id <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        SmsHomeRecommendProductDO exist = smsHomeRecommendProductMapper.selectById(id);
        if (Objects.isNull(exist)) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        smsHomeRecommendProductMapper.deleteById(id);
        return Response.success();
    }

    @Override
    public Response findRecommendProductOptions() {
        List<SmsHomeRecommendProductDO> list = smsHomeRecommendProductMapper.selectActiveList();

        List<HomeRecommendProductOptionVO> options = list.stream()
                .map(o -> HomeRecommendProductOptionVO.builder()
                        .id(o.getId())
                        .productId(o.getProductId())
                        .productName(o.getProductName())
                        .sort(o.getSort())
                        .build())
                .collect(Collectors.toList());

        return Response.success(options);
    }
}

