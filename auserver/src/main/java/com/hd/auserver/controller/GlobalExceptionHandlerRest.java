package com.hd.auserver.controller;

import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import com.mongodb.DuplicateKeyException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandlerRest {
    @ExceptionHandler(value = AccessDeniedException.class)
    public RetResult defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {

        return RetResponse.makeErrRsp("拒绝访问!");
    }
    @ExceptionHandler(value = NullPointerException.class)
    public RetResult defaultErrorHandler4(HttpServletRequest req, Exception e) throws Exception {

        return RetResponse.makeErrRsp("服务器错误!");
    }

    @ExceptionHandler(value = Exception.class)
    public RetResult defaultErrorHandler5(HttpServletRequest req, Exception e) throws Exception {
        //主要，如果是InsufficientAuthenticationException，请不要返回错误，继续抛出异常，交由oauth处理，否则请求授权码模式到不了登录页面
        if(e instanceof InsufficientAuthenticationException){
            throw e;
        }
        return RetResponse.makeErrRsp("服务器错误!"+e.toString());
    }
    //拦截以下异常，获取验证码会报错：{"code":202,"msg":"服务器错误!org.springframework.security.authentication.InsufficientAuthenticationException: User must be authenticated with Spring Security before authorization can be completed."}
//    @ExceptionHandler(value = Exception.class)
//    public RetResult defaultErrorHandler6(HttpServletRequest req, Exception e) throws Exception {
//
//        return RetResponse.makeErrRsp("服务器错误!"+e.toString());
//    }
}