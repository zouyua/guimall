package com.gg.guimall.web.controller;

import com.gg.guimall.common.aspect.ApiOperationLog;
import com.gg.guimall.common.utils.Response;
import com.gg.guimall.web.service.SupportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 助农专区 Controller（前台）
 *
 * @author wly
 */
@RestController
@RequestMapping("/support")
@Api(tags = "助农专区")
public class SupportController {

    @Autowired
    private SupportService supportService;

    /**
     * 获取签约帮扶农户列表
     */
    @GetMapping("/farmers")
    @ApiOperation("获取签约帮扶农户列表")
    @ApiOperationLog(description = "获取签约帮扶农户列表")
    public Response findSupportFarmerList() {
        return supportService.findSupportFarmerList();
    }
}
