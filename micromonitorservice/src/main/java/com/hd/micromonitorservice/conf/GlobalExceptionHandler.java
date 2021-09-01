package com.hd.micromonitorservice.conf;

import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE+100)
public class GlobalExceptionHandler implements ResponseBodyAdvice<Object> {
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

        if(e instanceof BindException){
            return RetResponse.makeErrRsp(((BindException)e).getBindingResult().getFieldError().getDefaultMessage());
        }
        return RetResponse.makeErrRsp(e.getMessage());
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        //如果是返回了RetResult类就直接返回不做处理
        return o;
    }
}
