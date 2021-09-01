package com.hd.micromonitorservice.conf;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: liwei
 * @Description:  spring 初始化执行事件
 */
@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

    private volatile AtomicBoolean isInit=new AtomicBoolean(false);
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //保证执行一次
        if(!isInit.compareAndSet(false,true)) {
            return;
        }
        //isInit.set(false);
        // 初始化完成后.清空redis缓存
        delAllRedisKey();
    }

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 删除所有缓存
     */
    public void delAllRedisKey() {
        Set<String> keys = stringRedisTemplate.keys("*");
        if (ObjectUtils.isNotEmpty(keys)) {
            stringRedisTemplate.delete(keys);
        }
    }
}
