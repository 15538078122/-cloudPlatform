package com.hd.micromonitorservice.conf;

import com.hd.common.vo.SyMonitorVo;
import com.hd.micromonitorservice.service.SyMonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * @Author: liwei
 * @Description:
 */
@Configuration
@EnableScheduling
@Slf4j
public class ServiceMonitorTask {

    @Autowired
    SyMonitorService syMonitorService;

    @Scheduled(cron = "0/15 * * * * ?")
    private void beatTasks() {
        //System.err.println("执行heartbeat." + LocalDateTime.now());
        List<SyMonitorVo> syMonitorVos = syMonitorService.listServiceHeartbeat();
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(SyMonitorVo syMonitorVo:syMonitorVos){
            if(!syMonitorVo.getOnLine()){
                log.error(String.format("离线:%s",syMonitorVo.getShowName()));
            }
        }
    }
}
