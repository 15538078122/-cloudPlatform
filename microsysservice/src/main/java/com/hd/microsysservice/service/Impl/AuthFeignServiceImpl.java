package com.hd.microsysservice.service.Impl;

import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import com.hd.common.model.TokenInfo;
import com.hd.microsysservice.service.AuthFeignService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

@Component
public
class AuthFeignServiceImpl implements AuthFeignService {

    @Override
    public RetResult auth(@RequestBody TokenInfo tokenInfo) {
        return RetResponse.makeTimeOutRsp("sorry! 网络异常，服务暂时无法访问!");
    }
}
