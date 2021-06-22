package com.hd.gateway;

import com.hd.gateway.model.RetResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: liwei
 * @Description:
 */
@RestController
public class DefaultHystrixController {


    @RequestMapping("/defaultfallback")
    public Object defaultfallback(){
        //System.out.println("降级操作...");

        return new RetResult(HttpStatus.INTERNAL_SERVER_ERROR.value(),"服务接口访问异常","");
    }

    /*@RequestMapping("/defaultfallback")
    public Map<String,String> defaultfallback(){
        System.out.println("降级操作...");
        Map<String,String> map = new HashMap<>();
        map.put("resultCode","false");
        map.put("resultMessage","服务异常");
        map.put("resultObj","这里测试网关服务熔断");
        return map;
    }*/
}
