package com.hd.auservice;

import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

@Component
class AuthFeignService implements IAuthFeignService {

    @Override
    public RetResult auth(@RequestParam("account") String account, @RequestParam("scopes") String scopes,
                          @RequestParam("uri") String uri, @RequestParam("method") String method) {
        return RetResponse.makeTimeOutRsp("sorry! 网络异常，服务暂时无法访问!");
    }
}
