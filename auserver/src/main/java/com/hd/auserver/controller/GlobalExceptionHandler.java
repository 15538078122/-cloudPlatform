package com.hd.auserver.controller;

import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import com.mongodb.DuplicateKeyException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLIntegrityConstraintViolationException;


@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = AccessDeniedException.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("invalid");
        //TODO: 授权拒绝 返回页面
        return mav;
    }
    @ExceptionHandler(value = NullPointerException.class)
    public ModelAndView defaultErrorHandler4(HttpServletRequest req, Exception e) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("invalid");
        //TODO: 授权异常 返回页面
        return mav;
    }
//    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
//    public RetResult defaultErrorHandler5(HttpServletRequest req, Exception e) throws Exception {
//
//        return RetResponse.makeErrRsp("服务器错误!"+e.toString());
//    }
   //拦截以下异常，获取验证码会报错：{"code":202,"msg":"服务器错误!org.springframework.security.authentication.InsufficientAuthenticationException: User must be authenticated with Spring Security before authorization can be completed."}
//   @ExceptionHandler(value = Exception.class)
//   public ModelAndView defaultErrorHandler5(HttpServletRequest req, Exception e) throws Exception {
//
//       ModelAndView mav = new ModelAndView();
//       mav.addObject("exception", e);
//       mav.addObject("url", req.getRequestURL());
//       mav.setViewName("invalid");
//       //TODO: 授权异常 返回页面
//       return mav;
//   }
}
