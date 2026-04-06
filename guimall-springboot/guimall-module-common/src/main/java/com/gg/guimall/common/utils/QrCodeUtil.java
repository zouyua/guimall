package com.gg.guimall.common.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码生成工具类（基于 Google ZXing）
 *
 * @author wly
 * @date 2026/3/31
 */
@Slf4j
public class QrCodeUtil {

    /** 默认二维码宽度（像素） */
    private static final int DEFAULT_WIDTH = 300;
    /** 默认二维码高度（像素） */
    private static final int DEFAULT_HEIGHT = 300;

    /**
     * 根据内容生成二维码 PNG 字节数组
     *
     * @param content 二维码编码内容（通常为 URL）
     * @return PNG 格式的字节数组
     */
    public static byte[] generatePng(String content) {
        return generatePng(content, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * 根据内容生成指定尺寸的二维码 PNG 字节数组
     *
     * @param content 二维码编码内容
     * @param width   宽度（像素）
     * @param height  高度（像素）
     * @return PNG 格式的字节数组
     */
    public static byte[] generatePng(String content, int width, int height) {
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.MARGIN, 1);

            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            log.info("==> 二维码生成成功，内容: {}, 尺寸: {}x{}", content, width, height);
            return outputStream.toByteArray();
        } catch (WriterException | IOException e) {
            log.error("==> 二维码生成失败，内容: {}", content, e);
            throw new RuntimeException("二维码生成失败", e);
        }
    }
}
