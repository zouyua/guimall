package com.gg.guimall.web.controller;

import com.gg.guimall.common.utils.Response;
import com.gg.guimall.web.service.AlipayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/alipay")
@Api(tags = "支付宝支付")
public class AlipayController {

    @Autowired
    private AlipayService alipayService;

    @GetMapping("/pay")
    @ApiOperation("创建支付（返回支付宝支付表单HTML）")
    public Response pay(@RequestParam String orderSn) {
        return alipayService.createPay(orderSn);
    }

    @PostMapping("/notify")
    @ApiOperation("支付宝异步通知回调")
    public String notify(HttpServletRequest request) {
        return alipayService.handleNotify(request);
    }

    @GetMapping("/query")
    @ApiOperation("主动查询支付宝交易状态（同步回跳后调用）")
    public Response query(@RequestParam String orderSn) {
        return alipayService.queryAndUpdateOrder(orderSn);
    }
}
