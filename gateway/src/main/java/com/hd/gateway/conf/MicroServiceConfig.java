package com.hd.gateway.conf;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author: liwei
 * @Description:
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "config")
public class MicroServiceConfig {
    private Map<String, String> microServices;
}
