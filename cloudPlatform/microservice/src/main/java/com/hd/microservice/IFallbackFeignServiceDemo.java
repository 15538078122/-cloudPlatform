package com.hd.microservice;

import com.hd.common.RetResult;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@FeignClient(value = "microserv",fallback = FallbackFeignServiceDemo.class)
public interface IFallbackFeignServiceDemo {
    @GetMapping("/test2")
//    @HystrixCommand(commandKey = "microservtest2")
    public RetResult test2(@RequestParam("para") String para);
}
