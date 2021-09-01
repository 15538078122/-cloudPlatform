package com.hd.gateway.conf;

import com.hd.common.RetResult;
import com.hd.common.utils.HttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: liwei
 * @Description:
 */
@Configuration
@EnableScheduling
public class HeartBeatTask {
    @Value("${config.heart-beat-uri}")
    private String heartbeaturi;
    @Value("${spring.application.name}")
    private String appname;

    @Scheduled(cron = "0/15 * * * * ?")
    private void beatTasks(){
        System.err.println("执行heartbeat." + LocalDateTime.now());
        Map<String, String> params = new HashMap<String, String>();
        //params.put("account", tokenInfo.getAccount());
        try {
            RetResult retResult = HttpUtil.httpGet(heartbeaturi+"/"+appname,params);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
