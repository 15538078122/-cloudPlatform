package com.hd.microauservice.service.Impl;

import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import com.hd.microauservice.service.FallbackFeignServiceDemo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public  class FallbackFeignServiceDemoImpl implements FallbackFeignServiceDemo {
    @Override
    public RetResult test2(String para) {
        return RetResponse.makeTimeOutRsp("sorry! 网络异常，服务暂时无法访问!");
    }
}
