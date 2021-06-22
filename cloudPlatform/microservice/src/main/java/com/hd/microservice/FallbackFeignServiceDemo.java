package com.hd.microservice;

import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import org.springframework.stereotype.Component;

@Component
class FallbackFeignServiceDemo implements IFallbackFeignServiceDemo {
    @Override
    public RetResult test2(String para) {
        return RetResponse.makeTimeOutRsp("sorry! 网络异常，服务暂时无法访问!");
    }
}
