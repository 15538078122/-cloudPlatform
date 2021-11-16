package com.hd.microsysservice.conf;

import com.hd.common.RetResult;
import com.hd.common.utils.HttpUtil;
import com.hd.microsysservice.utils.LicenseCheckUtil;
import com.hd.microsysservice.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class HeartBeatTask  {

    @Autowired
    ConfigData configData;
    @Autowired
    ServerConfig serverConfig;
    @Autowired
    LicenseCheckUtil licenseCheckUtil;

    private volatile AtomicBoolean scanDone=new AtomicBoolean(false);

    @Scheduled(cron = "0/15 * * * * ?")
    private void beatTasks(){
        //System.err.println("执行heartbeat." + LocalDateTime.now());
        log.debug("执行heartbeat.");
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        //MultiValueMap<String, String> params = new HashMap<String, String>();
        params.add("serviceName", configData.getAppname());
        String clientId=serverConfig.getUrl();
        params.add("clientId", clientId);
        try {
            ConfigData configData44= (ConfigData) SpringContextUtil.getBean("configData");
            RetResult retResult = HttpUtil.httpPost(configData.getHeartbeaturi(),params);
        }
        catch (Exception e) {
            //e.printStackTrace();
            log.error("执行heartbeat 失败.");
        }
        //保证执行一次
        if(scanDone.compareAndSet(false,true)) {
            licenseCheckUtil.checkAllLicense();
        }
    }

}
