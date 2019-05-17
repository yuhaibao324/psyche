package com.liangliang.fastbase.exception;

import com.fasterxml.jackson.annotation.JsonCreator;

import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

public enum GlobalErrorCode implements ErrorCode{

    /**
     * Generic Error
     */

    SUCCESS(1, "OK"),
    FAILURE(0, "Failure"),

    // HTTP Request Error
    BAD_REQUEST(400, "Bad Request"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    PARAMS_ERROR(406, "params error"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    ;

    GlobalErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private final int code;

    private final String message;
    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    private static Map<Integer, GlobalErrorCode> map =new HashMap<>(GlobalErrorCode.values().length);

    static {
        for (GlobalErrorCode errorCode : GlobalErrorCode.values()) {
            map.put(errorCode.getCode(), errorCode);
        }
    }

    @JsonCreator
    public static GlobalErrorCode getInstance(int value) {
        GlobalErrorCode instance = map.get(value);
        Assert.notNull(instance, "not defined Enum");
        return instance;
    }
}
