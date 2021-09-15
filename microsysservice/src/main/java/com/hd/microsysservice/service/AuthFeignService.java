package com.hd.microsysservice.service;

import com.hd.common.RetResult;
import com.hd.common.model.TokenInfo;
import com.hd.microsysservice.service.Impl.AuthFeignServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@FeignClient(value = "microsys",fallback = AuthFeignServiceImpl.class)
public interface AuthFeignService {
    @PostMapping("/sys/auth")
    public RetResult auth(@RequestBody TokenInfo tokenInfo);
}
