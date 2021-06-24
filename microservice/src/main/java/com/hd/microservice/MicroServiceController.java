package com.hd.microservice;

import com.alibaba.fastjson.JSON;
import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RefreshScope
@RestController
@Slf4j
public class MicroServiceController {

    @Value("${server.port}")
    String port;

    @Value("${custom.property}")
    String customProperty;

    @Autowired
    IFallbackFeignServiceDemo iFallbackFeignServiceDemo;

    @RequestMapping("/test")
    public Object test(@RequestParam("para") String para) throws Exception {

        //大于3s，会触发retry和熔断策略
        Thread.sleep(2000);
        log.info("invoke test:"+Thread.currentThread().getId());
        //RetResult xx = iFallbackFeignServiceDemo.test2("test2");
        //log.info(JSON.toJSONString(xx));
        return para+"from :"+port;
    }

    @RequestMapping("/test2")
    public RetResult test2(@RequestParam("para") String para) throws Exception {
        //int dd = 1 / 0;
        //Thread.sleep(4000);
//        int i = 0;
//        while (i++ < Integer.MAX_VALUE / 5) {
//            i += i * (new Random()).nextInt();
//        }

        return RetResponse.makeRsp(new Date().toString() + ": " +para);
    }
}