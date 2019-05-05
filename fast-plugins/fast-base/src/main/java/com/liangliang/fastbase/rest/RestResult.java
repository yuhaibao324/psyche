package com.liangliang.fastbase.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.liangliang.fastbase.exception.GlobalErrorCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 功能描述: <br>
 *
 * @since: 1.0.0
 * @Author: sunliang
 * @Date: 2018/7/9
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestResult<T extends Object> implements Serializable {

    /**
     * 返回编码
     */
    @JSONField(ordinal = 1)
    private int code;

    /**
     * 返回消息
     */
    @JSONField(ordinal = 2)
    private String message;

    /**
     * 返回数据
     */
    @JSONField(ordinal = 3)
    private T data;

    public RestResult() {

    }

    public RestResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public RestResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public RestResult(GlobalErrorCode err) {
        this.code = err.getCode();
        this.message = err.getMessage();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return StringUtils.trimToEmpty(message);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RestResult(code=" + this.code + ", message=" + this
                .message + ", data=" + JSON.toJSONString(this.data) + ")";
    }

}
