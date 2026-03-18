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

    // ----------- 满减异常状态码 -----------
    FULL_REDUCTION_NOT_FOUND("30020", "满减规则不存在"),
    ;

    // 异常码
    private String errorCode;
    // 错误信息
    private String errorMessage;

}