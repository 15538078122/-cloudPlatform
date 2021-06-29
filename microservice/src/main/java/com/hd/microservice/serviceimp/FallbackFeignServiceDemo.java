package com.hd.microservice.serviceimp;

import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import com.hd.microservice.iservice.IFallbackFeignServiceDemo;
import org.springframework.stereotype.Component;

@Component
public  class FallbackFeignServiceDemo implements IFallbackFeignServiceDemo {
    @Override
    public RetResult test2(String para) {
        return RetResponse.makeTimeOutRsp("sorry! 网络异常，服务暂时无法访问!");
    }
}
