package com.hd.microsysservice.conf;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @Author: liwei
 * @Description:
 */
@Component
@Setter
@Getter
@RefreshScope
public class ConfigData{

    @Value("${config.heart-beat-uri}")
    private  String heartbeaturi;

    public String getHeartbeaturi(){
        return heartbeaturi;
    }

    @Value("${spring.application.name}")
    private String appname;

}
