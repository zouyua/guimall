package com.gg.guimall.admin.controller;

import com.gg.guimall.admin.model.vo.sms.FindSmsCouponHistoryPageListReqVO;
import com.gg.guimall.admin.model.vo.sms.FindSmsCouponHistoryPageListRspVO;
import com.gg.guimall.admin.service.SmsCouponHistoryService;
import com.gg.guimall.common.aspect.ApiOperationLog;
import com.gg.guimall.common.utils.PageResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 优惠券领取记录管理 Controller
 *
 * @author: wly
 * @url: www.gg.com
 * @date: 2026/3/19
 */
@RestController
@RequestMapping("/admin/sms/couponHistory")
@Api(tags = "优惠券领取记录管理模块")
public class SmsCouponHistoryController {

    @Autowired
    private SmsCouponHistoryService couponHistoryService;

    @GetMapping("/list")
    @ApiOperation(value = "优惠券领取记录列表分页查询")
    @ApiOperationLog(description = "优惠券领取记录列表分页查询")
    public PageResponse<FindSmsCouponHistoryPageListRspVO> list(FindSmsCouponHistoryPageListReqVO reqVO) {
        return couponHistoryService.findCouponHistoryPageList(reqVO);
    }
}

