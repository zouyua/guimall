package com.gg.guimall.common.exception;

import lombok.Getter;
import lombok.Setter;

/*
* @author:wly
* @url:www.gg.com
* @date:2025/12/20
* @description:业务异常*/
@Getter
@Setter
public class BizException extends RuntimeException {
    //异常码
    private String errorCode;
    //错误信息
    private String errorMessage;
    public BizException(BaseExceptionInterface baseExceptionInterface)
    {
        super(baseExceptionInterface.getErrorMessage());
        this.errorCode = baseExceptionInterface.getErrorCode();
        this.errorMessage = baseExceptionInterface.getErrorMessage();
    }
}
