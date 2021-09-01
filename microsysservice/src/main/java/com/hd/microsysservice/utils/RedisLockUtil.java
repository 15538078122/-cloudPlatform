package com.hd.microsysservice.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @Author: liwei
 * @Description: 分布锁
 */
@Component
@Slf4j
public class RedisLockUtil {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public boolean lock(String key, String value, long timeout, TimeUnit timeUnit) {
        // 加锁，也可以使用 stringRedisTemplate.opsForValue().setIfAbsent(key, value, 15, TimeUnit.SECONDS);
        // Expiration.from(timeout, timeUnit) 过期时间和单位
        // RedisStringCommands.SetOption.SET_IF_ABSENT ,等同于 NX 当 key 不存在时候才可以
        Boolean lockStat = stringRedisTemplate.execute((RedisCallback<Boolean>) connection ->
                connection.set(key.getBytes(StandardCharsets.UTF_8), value.getBytes(StandardCharsets.UTF_8),
                        Expiration.from(timeout, timeUnit), RedisStringCommands.SetOption.SET_IF_ABSENT));
        return lockStat != null && lockStat;
    }

    public boolean unlock(String key, String value) {
        try {
            // 释放锁使用 Lua 脚本，验证传入的 key--value 跟 Redis 中是否一样
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Boolean unLockStat = stringRedisTemplate.execute((RedisCallback<Boolean>) connection ->
                    connection.eval(script.getBytes(), ReturnType.BOOLEAN, 1,
                            key.getBytes(StandardCharsets.UTF_8), value.getBytes(StandardCharsets.UTF_8)));
            return unLockStat == null || unLockStat;
        } catch (Exception e) {
            log.error("解锁失败 key = {}", key);
            return false;
        }
    }
}
