package com.gg.guimall.web.controller;

import com.gg.guimall.common.utils.MinioUtil;
import com.gg.guimall.common.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
@Api(tags = "文件上传")
@Slf4j
public class FileUploadController {

    @Autowired
    private MinioUtil minioUtil;

    @PostMapping("/upload")
    @ApiOperation("上传文件（图片等）")
    public Response upload(@RequestParam("file") MultipartFile file) {
        try {
            String url = minioUtil.uploadFile(file);
            return Response.success(url);
        } catch (Exception e) {
            log.error("文件上传失败", e);
            return Response.fail("文件上传失败: " + e.getMessage());
        }
    }
}
