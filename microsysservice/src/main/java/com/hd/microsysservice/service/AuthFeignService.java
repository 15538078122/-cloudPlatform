package com.hd.microsysservice.service;

import com.hd.common.RetResult;
import com.hd.microsysservice.service.Impl.AuthFeignServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@FeignClient(value = "microsys",fallback = AuthFeignServiceImpl.class)
public interface AuthFeignService {
    @PostMapping("/auth")
    public RetResult auth(@RequestParam("account") String account, @RequestParam("scopes") String scopes,
                          @RequestParam("uri") String uri, @RequestParam("method") String method, @RequestParam("enterId") String enterpriseId);
}
