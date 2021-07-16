package com.hd.microauservice.conf;

import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 参数不匹配异常
     */
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = { MethodArgumentNotValidException.class})
    public RetResult methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        //return RetJson.fail(ResultCode.VALIDATE_FAILED.getCode(), "参数异常"+e.getBindingResult().getFieldError().getDefaultMessage());
        //getDefaultMessage()会返回message信息
        return RetResponse.makeErrRsp(e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public RetResult defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {

        return RetResponse.makeErrRsp(e.toString());
    }
}
