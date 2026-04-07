package com.gg.guimall.common.enums;

import com.gg.guimall.common.exception.BaseExceptionInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;

/*
* @author:wly
* @url:www.gg.com
* @date:2025/12/20
* @description:响应异常码*/
@Getter
@AllArgsConstructor
public enum ResponseCodeEnum implements BaseExceptionInterface {

    // ----------- 通用异常状态码 -----------
    SYSTEM_ERROR("10000", "出错啦，后台小哥正在努力修复中..."),
    PARAM_NOT_VALID("10001", "参数错误"),


    // ----------- 业务异常状态码 -----------
    LOGIN_FAIL("20000", "登录失败"),
    USERNAME_OR_PWD_ERROR("20001", "用户名或密码错误"),
    UNAUTHORIZED("20002", "无访问权限，请先登录！"),
    FORBIDDEN("20004", "游客账号仅支持查询操作！"),
    USERNAME_NOT_FOUND("20003","该用户不存在"),

    // ----------- 商品管理异常状态码 -----------
    PRODUCT_NOT_FOUND("30001", "商品不存在"),
    PRODUCT_CATEGORY_NOT_FOUND("30002", "商品分类不存在"),
    PRODUCT_CREATE_FAIL("30003", "商品创建失败"),
    PRODUCT_UPDATE_FAIL("30004", "商品修改失败"),
    PRODUCT_DELETE_FAIL("30005", "商品删除失败"),
    INVALID_PRODUCT_DATA("30006", "商品数据无效"),
    /** 该分类下存在子分类，无法删除 */
    CATEGORY_HAS_CHILDREN("30012", "该分类下存在子分类，无法删除"),
    /** 该分类下存在关联商品，无法删除 */
    CATEGORY_HAS_PRODUCTS("30013", "该分类下存在关联商品，无法删除"),

    // ----------- 农户管理异常状态码 -----------
    FARMER_NOT_FOUND("30007", "农户不存在"),
    FARMER_NAME_EXISTS("30008", "农户名称已存在"),
    FARMER_CREATE_FAIL("30009", "农户创建失败"),
    FARMER_UPDATE_FAIL("30010", "农户修改失败"),
    FARMER_DELETE_FAIL("30011", "农户删除失败"),

    // ----------- 商品属性管理异常状态码 -----------
    ATTRIBUTE_CATEGORY_NOT_FOUND("30014", "属性分类不存在"),
    ATTRIBUTE_CATEGORY_NAME_EXISTS("30015", "属性分类名称已存在"),
    ATTRIBUTE_NOT_FOUND("30016", "商品属性不存在"),
    ATTRIBUTE_NAME_EXISTS("30017", "属性名称已存在"),
    ATTRIBUTE_IN_USE("30018", "属性正在使用中，无法删除"),

    // ----------- SKU库存异常状态码 -----------
    SKU_NOT_FOUND("30019", "SKU不存在"),
    STOCK_NOT_ENOUGH("30021", "库存不足"),

    // ----------- 满减异常状态码 -----------
    FULL_REDUCTION_NOT_FOUND("30020", "满减规则不存在"),

    // ----------- 订单异常状态码 -----------
    ORDER_NOT_FOUND("40001", "订单不存在"),
    ORDER_STATUS_ILLEGAL("40002", "订单状态不允许该操作"),

    // ----------- 退货异常状态码 -----------
    ORDER_RETURN_NOT_FOUND("40003", "退货申请不存在"),
    ORDER_RETURN_STATUS_ILLEGAL("40004", "退货申请状态不允许该操作"),

    // ----------- 退货原因异常状态码 -----------
    ORDER_RETURN_REASON_NOT_FOUND("40005", "退货原因不存在"),

    // ----------- 购物车异常状态码 -----------
    CART_ITEM_NOT_FOUND("40006", "购物车商品不存在"),

    // ----------- 产地/溯源异常状态码 -----------
    ORIGIN_NOT_FOUND("50001", "产地不存在"),
    ORIGIN_NAME_EXISTS("50002", "产地名称已存在"),
    PRODUCT_ORIGIN_NOT_FOUND("50003", "商品产地关联不存在"),
    TRACE_RECORD_NOT_FOUND("50004", "溯源记录不存在"),
    TRACE_QRCODE_NOT_FOUND("50005", "溯源二维码不存在"),

    // ----------- 营销/优惠券异常状态码 -----------
    COUPON_NOT_FOUND("60001", "优惠券不存在"),
    COUPON_NOT_EXIST("60001", "优惠券不存在"),
    COUPON_NOT_START("60002", "优惠券领取未开始"),
    COUPON_EXPIRED("60003", "优惠券已过期"),
    COUPON_OUT_OF_STOCK("60004", "优惠券已领完"),
    COUPON_RECEIVE_LIMIT("60005", "已达到领取上限"),
    COUPON_HISTORY_NOT_EXIST("60006", "优惠券领取记录不存在"),
    COUPON_NOT_BELONG_TO_MEMBER("60007", "该优惠券不属于当前会员"),
    COUPON_ALREADY_USED("60008", "优惠券已使用或已过期"),

    ;

    // 异常码
    private String errorCode;
    // 错误信息
    private String errorMessage;

}