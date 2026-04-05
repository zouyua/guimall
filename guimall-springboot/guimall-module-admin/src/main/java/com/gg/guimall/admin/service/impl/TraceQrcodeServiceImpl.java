package com.gg.guimall.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gg.guimall.admin.model.vo.trace.FindTraceQrcodeRspVO;
import com.gg.guimall.admin.model.vo.trace.UpsertTraceQrcodeReqVO;
import com.gg.guimall.admin.service.TraceQrcodeService;
import com.gg.guimall.common.domain.dos.PmsProductDO;
import com.gg.guimall.common.domain.dos.TraceQrcodeDO;
import com.gg.guimall.common.domain.mapper.PmsProductMapper;
import com.gg.guimall.common.domain.mapper.TraceQrcodeMapper;
import com.gg.guimall.common.enums.ResponseCodeEnum;
import com.gg.guimall.common.exception.BizException;
import com.gg.guimall.common.utils.MinioUtil;
import com.gg.guimall.common.utils.QrCodeUtil;
import com.gg.guimall.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 溯源二维码 Service 实现类
 * <p>
 * generate 方法：自动根据 productId 拼接溯源 URL → ZXing 生成二维码 → 上传 MinIO → 写入 trace_qrcode 表
 *
 * @author wly
 * @url www.gg.com
 * @date 2026/3/19
 */
@Service
@Slf4j
public class TraceQrcodeServiceImpl implements TraceQrcodeService {

    @Autowired
    private TraceQrcodeMapper traceQrcodeMapper;

    @Autowired
    private PmsProductMapper pmsProductMapper;

    @Autowired
    private MinioUtil minioUtil;

    /** 前端溯源页面基础地址，从 application-dev.yml 读取 */
    @Value("${trace.frontend-base-url:http://localhost:5173}")
    private String frontendBaseUrl;

    @Override
    public Response upsert(UpsertTraceQrcodeReqVO reqVO) {

        PmsProductDO productDO = pmsProductMapper.selectById(reqVO.getProductId());
        if (Objects.isNull(productDO)) {
            throw new BizException(ResponseCodeEnum.PRODUCT_NOT_FOUND);
        }

        TraceQrcodeDO exist = traceQrcodeMapper.selectByProductId(reqVO.getProductId());
        if (Objects.nonNull(exist)) {
            TraceQrcodeDO update = TraceQrcodeDO.builder()
                    .id(exist.getId())
                    .productId(reqVO.getProductId())
                    .qrcodeUrl(reqVO.getQrcodeUrl())
                    .traceUrl(reqVO.getTraceUrl())
                    .build();
            traceQrcodeMapper.updateById(update);
            return Response.success();
        }

        TraceQrcodeDO insert = TraceQrcodeDO.builder()
                .productId(reqVO.getProductId())
                .qrcodeUrl(reqVO.getQrcodeUrl())
                .traceUrl(reqVO.getTraceUrl())
                .scanCount(0)
                .build();
        traceQrcodeMapper.insert(insert);
        return Response.success();
    }

    @Override
    public Response generate(Long productId) {

        if (Objects.isNull(productId) || productId <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        // 1. 校验商品存在
        PmsProductDO productDO = pmsProductMapper.selectById(productId);
        if (Objects.isNull(productDO)) {
            throw new BizException(ResponseCodeEnum.PRODUCT_NOT_FOUND);
        }

        // 2. 拼接溯源页 URL（消费者扫码后跳转到的地址）
        String traceUrl = frontendBaseUrl + "/trace/" + productId;

        // 3. ZXing 生成二维码 PNG 字节数组
        byte[] qrCodePng = QrCodeUtil.generatePng(traceUrl);

        // 4. 上传到 MinIO
        String qrcodeUrl;
        try {
            qrcodeUrl = minioUtil.uploadBytes(qrCodePng, ".png", "image/png");
        } catch (Exception e) {
            log.error("==> 二维码上传 MinIO 失败, productId: {}", productId, e);
            throw new RuntimeException("二维码上传失败", e);
        }

        // 5. 写入或更新 trace_qrcode 表
        TraceQrcodeDO exist = traceQrcodeMapper.selectByProductId(productId);
        if (Objects.nonNull(exist)) {
            TraceQrcodeDO update = TraceQrcodeDO.builder()
                    .id(exist.getId())
                    .traceUrl(traceUrl)
                    .qrcodeUrl(qrcodeUrl)
                    .build();
            traceQrcodeMapper.updateById(update);
        } else {
            TraceQrcodeDO insert = TraceQrcodeDO.builder()
                    .productId(productId)
                    .traceUrl(traceUrl)
                    .qrcodeUrl(qrcodeUrl)
                    .scanCount(0)
                    .build();
            traceQrcodeMapper.insert(insert);
        }

        // 6. 返回生成结果
        FindTraceQrcodeRspVO rspVO = FindTraceQrcodeRspVO.builder()
                .productId(productId)
                .traceUrl(traceUrl)
                .qrcodeUrl(qrcodeUrl)
                .scanCount(Objects.nonNull(exist) ? exist.getScanCount() : 0)
                .build();
        return Response.success(rspVO);
    }

    @Override
    public Response findByProductId(Long productId) {

        if (Objects.isNull(productId) || productId <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        TraceQrcodeDO qrcodeDO = traceQrcodeMapper.selectByProductId(productId);
        if (Objects.isNull(qrcodeDO)) {
            throw new BizException(ResponseCodeEnum.TRACE_QRCODE_NOT_FOUND);
        }

        FindTraceQrcodeRspVO rspVO = new FindTraceQrcodeRspVO();
        BeanUtils.copyProperties(qrcodeDO, rspVO);
        return Response.success(rspVO);
    }

    @Override
    public Response deleteByProductId(Long productId) {

        if (Objects.isNull(productId) || productId <= 0) {
            throw new BizException(ResponseCodeEnum.PARAM_NOT_VALID);
        }

        traceQrcodeMapper.delete(new LambdaQueryWrapper<TraceQrcodeDO>()
                .eq(TraceQrcodeDO::getProductId, productId));
        return Response.success();
    }
}
