package com.gg.guimall.admin.controller;

import com.gg.guimall.admin.model.vo.sms.CreateSmsCouponReqVO;
import com.gg.guimall.admin.model.vo.sms.FindSmsCouponPageListReqVO;
import com.gg.guimall.admin.model.vo.sms.FindSmsCouponPageListRspVO;
import com.gg.guimall.admin.model.vo.sms.UpdateSmsCouponReqVO;
import com.gg.guimall.admin.service.SmsCouponService;
import com.gg.guimall.common.aspect.ApiOperationLog;
import com.gg.guimall.common.utils.PageResponse;
import com.gg.guimall.common.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 优惠券管理 Controller
 *
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 */
@RestController
@RequestMapping("/admin/sms/coupon")
@Api(tags = "优惠券管理模块")
public class SmsCouponController {

    @Autowired
    private SmsCouponService couponService;

    @PostMapping("/create")
    @ApiOperation(value = "创建优惠券")
    @ApiOperationLog(description = "创建优惠券")
    public Response create(@RequestBody @Validated CreateSmsCouponReqVO reqVO) {
        return couponService.createCoupon(reqVO);
    }

    @PostMapping("/delete/{id}")
    @ApiOperation(value = "删除优惠券")
    @ApiOperationLog(description = "删除优惠券")
    public Response delete(@PathVariable Long id) {
        return couponService.deleteCoupon(id);
    }

    @GetMapping("/list")
    @ApiOperation(value = "优惠券列表分页查询")
    @ApiOperationLog(description = "优惠券列表分页查询")
    public PageResponse<FindSmsCouponPageListRspVO> list(FindSmsCouponPageListReqVO reqVO) {
        return couponService.findCouponPageList(reqVO);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "优惠券详情")
    @ApiOperationLog(description = "优惠券详情")
    public Response detail(@PathVariable Long id) {
        return couponService.findCouponDetail(id);
    }

    @PostMapping("/update/{id}")
    @ApiOperation(value = "修改优惠券")
    @ApiOperationLog(description = "修改优惠券")
    public Response update(@PathVariable Long id,
                             @RequestBody @Validated UpdateSmsCouponReqVO reqVO) {
        reqVO.setId(id);
        return couponService.updateCoupon(reqVO);
    }
}

