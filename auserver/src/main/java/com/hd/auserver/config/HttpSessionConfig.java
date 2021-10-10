package com.hd.auserver.config;

/**
 * @Author: liwei
 * @Description:
 */
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds=60*30)//设置session过期时间,默认也是1800秒
public class HttpSessionConfig {

    //enableRedisKeyspaceNotificationsInitializer
    @Bean
    public static ConfigureRedisAction configureRedisAction() {
        return ConfigureRedisAction.NO_OP;
    }
}
