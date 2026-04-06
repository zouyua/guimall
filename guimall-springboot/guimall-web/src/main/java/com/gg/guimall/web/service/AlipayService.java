package com.gg.guimall.web.service;

import com.gg.guimall.common.utils.Response;
import javax.servlet.http.HttpServletRequest;

public interface AlipayService {
    /** 根据订单编号创建支付宝支付表单（PC网页支付） */
    Response createPay(String orderSn);

    /** 处理支付宝异步通知（验签+更新订单状态） */
    String handleNotify(HttpServletRequest request);

    /** 主动查询支付宝交易状态并更新订单（同步回跳后调用） */
    Response queryAndUpdateOrder(String orderSn);
}
