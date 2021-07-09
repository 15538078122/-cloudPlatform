package com.hd.microauservice.service;

import com.hd.common.RetResult;
import com.hd.microauservice.service.Impl.FallbackFeignServiceDemoImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@FeignClient(value = "microauserv",fallback = FallbackFeignServiceDemoImpl.class)
public interface FallbackFeignServiceDemo {
    @GetMapping("/test2")
//    @HystrixCommand(commandKey = "microservtest2")
    public RetResult test2(@RequestParam("para") String para);
}
