package com.hd.micromonitorservice.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @Author: liwei
 * @Description:
 */
@Component
public class UserCommonUtil {

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 返回在线用户数目
     * @return
     */
    public Integer getOnLineUserCount() {
        Set<String> keys = redisTemplate.keys(String.format("edgeOut:%s", "*"));
        Integer onlineCount=0;
        for (String key : keys)
        {
            Boolean edgeOut=(Boolean)  redisTemplate.opsForValue().get(key);
            if(!edgeOut){
                onlineCount++;
            }
        }
        return  onlineCount;
    }
}
