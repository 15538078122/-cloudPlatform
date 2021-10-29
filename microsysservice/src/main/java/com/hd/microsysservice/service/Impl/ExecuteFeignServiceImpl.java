package com.hd.microsysservice.service.Impl;

import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import com.hd.microsysservice.service.ExecuteFeignService;
import org.springframework.stereotype.Component;

@Component
public
class ExecuteFeignServiceImpl implements ExecuteFeignService {

    @Override
    public RetResult undone(Long userId) {
        return RetResponse.makeTimeOutRsp("sorry! 网络异常，服务暂时无法访问!");
    }
}
