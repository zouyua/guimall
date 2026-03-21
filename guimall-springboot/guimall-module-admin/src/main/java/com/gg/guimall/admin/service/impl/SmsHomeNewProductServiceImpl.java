package com.gg.guimall.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gg.guimall.admin.model.vo.sms.CreateSmsHomeNewProductReqVO;
import com.gg.guimall.admin.model.vo.sms.FindSmsHomeNewProductDetailRspVO;
import com.gg.guimall.admin.model.vo.sms.FindSmsHomeNewProductPageListReqVO;
import com.gg.guimall.admin.model.vo.sms.FindSmsHomeNewProductPageListRspVO;
import com.gg.guimall.admin.model.vo.sms.HomeNewProductOptionVO;
import com.gg.guimall.admin.model.vo.sms.UpdateSmsHomeNewProductReqVO;
import com.gg.guimall.admin.service.SmsHomeNewProductService;
import com.gg.guimall.common.domain.dos.PmsProductDO;
import com.gg.guimall.common.domain.dos.SmsHomeNewProductDO;
import com.gg.guimall.common.domain.mapper.PmsProductMapper;
import com.gg.guimall.common.domain.mapper.SmsHomeNewProductMapper;
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
 * 新品推荐管理 Service 实现类
 */
@Service
@Slf4j
public class SmsHomeNewProductServiceImpl implements SmsHomeNewProductService {

    @Autowired
    private SmsHomeNewProductMapper smsHomeNewProductMapper;

    @Autowired
    private PmsProductMapper pmsProductMapper;

    @Override
    public Response createNewProduct(CreateSmsHomeNewProductReqVO reqVO) {
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
                !Objects.equals(productDO.getDeleteStatus(), 0) ||
                !Objects.equals(productDO.getPublishStatus(), 1)) {
            throw new BizException(ResponseCodeEnum.PRODUCT_NOT_FOUND);
        }

        // 以商品表为准，避免 productName 不一致
        SmsHomeNewProductDO homeDO = SmsHomeNewProductDO.builder()
                .productId(reqVO.getProductId())
                .productName(productDO.getName())
                .recommendStatus(reqVO.getRecommendStatus())
                .sort(reqVO.getSort())
                .build();

        smsHomeNewProductMapper.insert(homeDO);
        return Response.success();
    }

    @Override
    public PageResponse<FindSmsHomeNewProductPageListRspVO> findNewProductPageList(FindSmsHomeNewProductPageListReqVO reqVO) {
        Page<SmsHomeNewProductDO> page = smsHomeNewProductMapper.selectPageList(
                reqVO.getCurrent(),
                reqVO.getSize(),
                reqVO.getProductName(),
                reqVO.getRecommendStatus()
        );

        List<FindSmsHomeNewProductPageListRspVO> voList = page.getRecords().stream()
                .map(item -> {
                    FindSmsHomeNewProductPageListRspVO vo = new FindSmsHomeNewProductPageListRspVO();
                    BeanUtils.copyProperties(item, vo);
                    return vo;
                })
                .collect(Collectors.toList());

        return PageResponse.success(page, voList);
    }

    @Override
    public Response findNewProductDetail(Long id) {
        if (Objects.isNull(id) || id <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        SmsHomeNewProductDO homeDO = smsHomeNewProductMapper.selectById(id);
        if (Objects.isNull(homeDO)) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        FindSmsHomeNewProductDetailRspVO rspVO = new FindSmsHomeNewProductDetailRspVO();
        BeanUtils.copyProperties(homeDO, rspVO);
        return Response.success(rspVO);
    }

    @Override
    public Response updateNewProduct(UpdateSmsHomeNewProductReqVO reqVO) {
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

        SmsHomeNewProductDO exist = smsHomeNewProductMapper.selectById(reqVO.getId());
        if (Objects.isNull(exist)) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        PmsProductDO productDO = pmsProductMapper.selectById(reqVO.getProductId());
        if (Objects.isNull(productDO) ||
                !Objects.equals(productDO.getDeleteStatus(), 0) ||
                !Objects.equals(productDO.getPublishStatus(), 1)) {
            throw new BizException(ResponseCodeEnum.PRODUCT_NOT_FOUND);
        }

        SmsHomeNewProductDO updateDO = SmsHomeNewProductDO.builder()
                .id(reqVO.getId())
                .productId(reqVO.getProductId())
                .productName(productDO.getName())
                .recommendStatus(reqVO.getRecommendStatus())
                .sort(reqVO.getSort())
                .build();

        smsHomeNewProductMapper.updateById(updateDO);
        return Response.success();
    }

    @Override
    public Response deleteNewProduct(Long id) {
        if (Objects.isNull(id) || id <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        SmsHomeNewProductDO exist = smsHomeNewProductMapper.selectById(id);
        if (Objects.isNull(exist)) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        smsHomeNewProductMapper.deleteById(id);
        return Response.success();
    }

    @Override
    public Response findNewProductOptions() {
        List<SmsHomeNewProductDO> list = smsHomeNewProductMapper.selectActiveList();

        List<HomeNewProductOptionVO> options = list.stream()
                .map(o -> HomeNewProductOptionVO.builder()
                        .id(o.getId())
                        .productId(o.getProductId())
                        .productName(o.getProductName())
                        .sort(o.getSort())
                        .build())
                .collect(Collectors.toList());

        return Response.success(options);
    }
}

