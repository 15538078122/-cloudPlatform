package com.hd.auservice.conf;

import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public RetResult defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {

        return RetResponse.makeErrRsp(e.toString());
    }
}
