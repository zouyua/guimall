package com.gg.guimall.web.controller;

import com.gg.guimall.common.aspect.ApiOperationLog;
import com.gg.guimall.common.utils.Response;
import com.gg.guimall.web.model.vo.coupon.ReceiveCouponReqVO;
import com.gg.guimall.web.model.vo.coupon.UseCouponReqVO;
import com.gg.guimall.web.service.SmsCouponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/21
 * @description: 优惠券 Controller（前台）
 **/
@RestController("webSmsCouponController")
@RequestMapping("/coupon")
@Api(tags = "优惠券模块")
public class SmsCouponController {

    @Autowired
    private SmsCouponService couponService;

    @GetMapping("/available/list")
    @ApiOperation("获取可领取的优惠券列表")
    @ApiOperationLog(description = "获取可领取的优惠券列表")
    public Response getAvailableCouponList(
            @ApiParam(value = "会员ID", required = true) @RequestParam Long memberId) {
        return couponService.findAvailableCouponList(memberId);
    }

    @PostMapping("/receive")
    @ApiOperation("领取优惠券")
    @ApiOperationLog(description = "领取优惠券")
    public Response receiveCoupon(@RequestBody @Validated ReceiveCouponReqVO reqVO) {
        return couponService.receiveCoupon(reqVO);
    }

    @GetMapping("/member/list")
    @ApiOperation("获取会员的优惠券列表")
    @ApiOperationLog(description = "获取会员的优惠券列表")
    public Response getMemberCouponList(
            @ApiParam(value = "会员ID", required = true) @RequestParam Long memberId,
            @ApiParam(value = "使用状态：0未使用1已使用2已过期，不传查询全部") @RequestParam(required = false) Integer useStatus) {
        return couponService.findMemberCouponList(memberId, useStatus);
    }

    @PostMapping("/use")
    @ApiOperation("使用优惠券")
    @ApiOperationLog(description = "使用优惠券")
    public Response useCoupon(@RequestBody @Validated UseCouponReqVO reqVO) {
        return couponService.useCoupon(reqVO);
    }

    @GetMapping("/order/available/list")
    @ApiOperation("获取订单可用的优惠券列表")
    @ApiOperationLog(description = "获取订单可用的优惠券列表")
    public Response getOrderAvailableCouponList(
            @ApiParam(value = "会员ID", required = true) @RequestParam Long memberId,
            @ApiParam(value = "订单总金额", required = true) @RequestParam BigDecimal totalAmount) {
        return couponService.findOrderAvailableCouponList(memberId, totalAmount);
    }
}
