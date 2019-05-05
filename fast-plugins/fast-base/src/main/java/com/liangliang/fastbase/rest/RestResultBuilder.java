package com.liangliang.fastbase.rest;

import com.liangliang.fastbase.exception.ErrorCode;
import com.liangliang.fastbase.exception.GlobalErrorCode;
import lombok.extern.slf4j.Slf4j;

/**
 * 功能描述: <br>
 *
 * @since: 1.0.0
 * @Author: sunliang
 * @Date: 2018/7/9
 */
@Slf4j
public class RestResultBuilder<T> {
    protected int code;

    protected String message;

    protected T data;

    public static RestResultBuilder builder() {
        RestResultBuilder restResultBuilder = new RestResultBuilder();
        return restResultBuilder;
    }

    public RestResultBuilder code(int code) {
        this.code = code;
        return this;
    }

    public RestResultBuilder message(String message) {
        this.message = message;
        return this;
    }

    public RestResultBuilder data(T data) {
        this.data = data;
        return success(data);
    }

    public RestResultBuilder errorCode(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        return this;
    }

    public RestResultBuilder success() {
        this.code = GlobalErrorCode.SUCCESS.getCode();
        this.message = GlobalErrorCode.SUCCESS.getMessage();
        return this;
    }

    public RestResultBuilder success(T data) {
        this.code = GlobalErrorCode.SUCCESS.getCode();
        this.message = GlobalErrorCode.SUCCESS.getMessage();
        this.data = data;
        return this;
    }

    public RestResultBuilder failure() {
        this.code = GlobalErrorCode.FAILURE.getCode();
        this.message = GlobalErrorCode.FAILURE.getMessage();
        return this;
    }

    public RestResultBuilder failure(T data) {
        this.code = GlobalErrorCode.FAILURE.getCode();
        this.message = GlobalErrorCode.FAILURE.getMessage();
        this.data = data;
        return this;
    }

    public RestResultBuilder result(boolean successful) {
        if (successful) {
            return this.success();
        } else {
            return this.failure();
        }
    }

    public RestResultBuilder success(Boolean result) {
        if (result.booleanValue() == Boolean.TRUE) {
            success();
        } else {
            failure();
        }
        return this;
    }

    public RestResult<T> build() {
        return new RestResult<T>(this.code, this.message, this.data);
    }

    public RestResult build(RestResult restResult) {
        return restResult;
    }
}
