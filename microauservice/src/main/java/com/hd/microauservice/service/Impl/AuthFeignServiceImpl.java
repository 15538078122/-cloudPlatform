package com.hd.microauservice.service.Impl;

import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import com.hd.microauservice.service.AuthFeignService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

@Component
public
class AuthFeignServiceImpl implements AuthFeignService {

    @Override
    public RetResult auth(@RequestParam("account") String account, @RequestParam("scopes") String scopes,
                          @RequestParam("uri") String uri, @RequestParam("method") String method, @RequestParam("enterId") String enterpriseId) {
        return RetResponse.makeTimeOutRsp("sorry! 网络异常，服务暂时无法访问!");
    }
}
