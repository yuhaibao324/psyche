package com.liangliang.fastbase.exception.handler;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.liangliang.fastbase.exception.ArgumentException;
import com.liangliang.fastbase.exception.BusinessException;
import com.liangliang.fastbase.exception.ErrorCode;
import com.liangliang.fastbase.exception.GlobalErrorCode;
import com.liangliang.fastbase.rest.RestResult;
import com.liangliang.fastbase.rest.RestResultBuilder;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.portable.ApplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

/**
 * 功能描述: <br>
 *
 * @since: 1.0.0
 * @Author: sunliang
 * @Date: 2018/7/9
 */
@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {


    @ExceptionHandler
    //@ResponseStatus(HttpStatus.BAD_REQUEST)
    private RestResult runtimeExceptionHandler(Exception ex) {
        log.info("---------> runtimeExceptionHandler, exception:{}", ex.getMessage());
        return RestResultBuilder.builder().failure().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    //@ResponseStatus(HttpStatus.BAD_REQUEST)
    private RestResult illegalParamsExceptionHandler(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        Map<String, String> errors = Maps.newHashMapWithExpectedSize(bindingResult
                .getFieldErrorCount());
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return RestResultBuilder.builder().errorCode(GlobalErrorCode.BAD_REQUEST).data
                (errors).build();
    }

    @ExceptionHandler(BindException.class)
    public RestResult bindExceptionHandler(BindException ex) {
        Map<String, String> errors = Maps.newHashMapWithExpectedSize(ex.getFieldErrorCount());
        for (FieldError fieldError : ex.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        log.info("---------> invalid request! fields ex:{}", JSON.toJSONString(errors));
        return RestResultBuilder.builder().errorCode(GlobalErrorCode.BAD_REQUEST).data
                (errors).build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    //@ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestResult messageNotReadableExceptionHandler(HttpMessageNotReadableException ex) {
        log.info("---------> json convert failure, exception:{}", ex.getMessage());
        return RestResultBuilder.builder().errorCode(GlobalErrorCode.BAD_REQUEST).build();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    //@ResponseStatus(HttpStatus.NOT_FOUND)
    public RestResult methodArgumentExceptionHandler(MethodArgumentTypeMismatchException ex) {
        log.error("---------> path variable, exception:{}", ex.getMessage());
        return RestResultBuilder.builder().errorCode(GlobalErrorCode.BAD_REQUEST)
                .message(ex.getMessage()).build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public RestResult illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        ex.printStackTrace();
        log.info("IllegalArgumentException exception:{}", ex.getMessage());
        if (ex instanceof ArgumentException) {
            ArgumentException exArg = (ArgumentException) ex;
            return RestResultBuilder.builder().errorCode(exArg.getErrorCode()).build();
        }
        return RestResultBuilder.builder().errorCode(GlobalErrorCode.BAD_REQUEST).build();
    }

    @ExceptionHandler(BusinessException.class)
    //@ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestResult businessExceptionHandler(BusinessException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        log.info("---------> business exception code:{}, message:{}",ex.getCode(), ex.getMessage());
        if (ex.getErrorCode() == null) {

            return RestResultBuilder.builder().code(ex.getCode()).message(ex.getMessage()).build();
        }
        return RestResultBuilder.builder().errorCode(ex.getErrorCode()).build();
    }

    @ExceptionHandler(ApplicationException.class)
    //@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestResult applicationExceptionHandler(ApplicationException ex) {
        log.error("---------> application exception message:" + ex.getMessage(), ex);
        return RestResultBuilder.builder().errorCode(GlobalErrorCode.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public RestResult noHandlerFoundException(NoHandlerFoundException ex) {
        log.info("noHandlerFoundException 404 error requestUrl:{}, method:{}, exception:{}",
                ex.getRequestURL(), ex.getHttpMethod(), ex.getMessage());
        return RestResultBuilder.builder().errorCode(GlobalErrorCode.NOT_FOUND).build();
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
    public RestResult httpRequestMethodHandler(HttpServletRequest request,
                                               HttpRequestMethodNotSupportedException ex) {
        //ex.printStackTrace();
        log.info("httpRequestMethodHandler 405 error requestUrl:{}, method:{}, exception:{}",
                request.getRequestURI(), ex.getMethod(), ex.getMessage());
        return RestResultBuilder.builder().errorCode(GlobalErrorCode.METHOD_NOT_ALLOWED).build();
    }

    /**
     * get Throwable StackTrace.
     *
     * @param t
     * @return
     */
    protected String getStackTrace(Throwable t) {
        PrintWriter pw = null;
        try {
            StringWriter sw = new StringWriter();
            pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            return sw.toString();
        } catch (Exception e) {
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
        return "";
    }
}