package com.liangliang.fastbase.exception;

/**
 * 功能描述: <br>
 *
 * @since: 1.0.0
 * @Author: sunliang
 * @Date: 2018/7/9
 */
public class ArgumentException extends java.lang.IllegalArgumentException {

    protected ErrorCode errorCode;

    public ArgumentException(ErrorCode errorCode) {
        super(formatMsg(errorCode));
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }

    public final static String formatMsg(ErrorCode errorCode) {
        return String.format("%s:%s", errorCode.getCode(), errorCode.getMessage());
    }
}