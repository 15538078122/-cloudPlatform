package com.hd.auservice;

import com.hd.common.RetResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@FeignClient(value = "auservice",fallback = AuthFeignService.class)
public interface IAuthFeignService {
    @GetMapping("/auth")
    public RetResult auth(@RequestParam("account") String account, @RequestParam("scopes") String scopes,
                          @RequestParam("uri") String uri, @RequestParam("method") String method);
}
