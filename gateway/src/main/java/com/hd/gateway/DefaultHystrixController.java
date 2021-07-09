package com.hd.gateway;

import com.hd.common.RetResult;
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
}
