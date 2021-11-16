package com.hd.microsysservice.conf;

import com.hd.microsysservice.utils.LicenseCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @Author: liwei
 * @Description:
 */
@Configuration
@EnableScheduling
@Slf4j
public class LicenseCheckTask {

    @Autowired
    LicenseCheckUtil licenseCheckUtil;

    @Scheduled(cron = "0 0 3 * * ?")
    private void checkTasks(){
        licenseCheckUtil.checkAllLicense();
    }



}
