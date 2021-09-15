package com.hd.microsysservice.conf;

import com.hd.common.RetResult;
import com.hd.common.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: liwei
 * @Description:
 */
@Configuration
@EnableScheduling
@Slf4j
public class HeartBeatTask  {

    @Autowired
    ConfigData configData;

    @Scheduled(cron = "0/15 * * * * ?")
    private void beatTasks(){
        //System.err.println("执行heartbeat." + LocalDateTime.now());
        log.debug("执行heartbeat.");
        Map<String, String> params = new HashMap<String, String>();
        //params.put("account", tokenInfo.getAccount());
        try {
            //configData= (ConfigData)SpringContextUtil.getBean("configData");
            RetResult retResult = HttpUtil.httpGet(configData.getHeartbeaturi()+"/"+configData.getAppname(),params);
        }
        catch (Exception e) {
            //e.printStackTrace();
            log.error("执行heartbeat 失败.");
        }
    }

}
