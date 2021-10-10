package com.hd.micromonitorservice.conf;

import com.hd.common.RetResult;
import com.hd.common.utils.HttpUtil;
import com.hd.micromonitorservice.service.MicroSysService;
import com.hd.micromonitorservice.utils.ApiUtilsScan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: liwei
 * @Description:
 */
@Configuration
@EnableScheduling
@Slf4j
public class HeartBeatTask {
    @Value("${config.heart-beat-uri}")
    private String heartbeaturi;
    @Value("${spring.application.name}")
    private String appname;
    @Autowired
    ServerConfig serverConfig;

    private volatile AtomicBoolean scanDone=new AtomicBoolean(false);

    @Scheduled(cron = "0/15 * * * * ?")
    private void beatTasks(){
        //System.err.println("执行heartbeat." + LocalDateTime.now());
        log.debug("执行heartbeat.");
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("serviceName", appname);
        String clientId=serverConfig.getUrl();
        params.add("clientId", clientId);
        try {
            RetResult retResult = HttpUtil.httpPost(heartbeaturi,params);
        }
        catch (Exception e) {
            //e.printStackTrace();
            log.error("执行heartbeat 失败.");
        }
        //保证执行一次
        if(scanDone.compareAndSet(false,true)) {
            apiUtilsScan.scanUri(microSysService);
        }

    }
    @Autowired
    MicroSysService microSysService;
    @Autowired
    ApiUtilsScan apiUtilsScan;
}
