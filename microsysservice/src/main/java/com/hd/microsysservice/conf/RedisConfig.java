package com.hd.microsysservice.conf;

/**
 * @Author: liwei
 * @Description:
 */

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        FastJson2JsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJson2JsonRedisSerializer<>(Object.class);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);

        // value序列化方式采用fastJson
        template.setValueSerializer(fastJsonRedisSerializer);
        // hash的value序列化方式采用fastJson
        template.setHashValueSerializer(fastJsonRedisSerializer);

        template.afterPropertiesSet();
        return template;
    }

    /*
     * 系统缓存框架使用的配置
     * */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        FastJson2JsonRedisSerializer jackson2JsonRedisSerializer = new FastJson2JsonRedisSerializer(Object.class);
        // 配置序列化
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        RedisCacheConfiguration redisCacheConfiguration = config.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .entryTtl(Duration.ofMinutes(20))
                .disableCachingNullValues()
                ;

        RedisCacheManager cacheManager = RedisCacheManager.builder(factory)
                .cacheDefaults(redisCacheConfiguration)
                .withInitialCacheConfigurations(getRedisCacheConfigurationMap())
                .build();
        return cacheManager;
    }
    private Map<String, RedisCacheConfiguration> getRedisCacheConfigurationMap() {
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
        //指定特定key的ttl时间
        redisCacheConfigurationMap.put("lazyCache", this.getRedisCacheConfigurationWithTtl(-1));
        return redisCacheConfigurationMap;
    }
    private RedisCacheConfiguration getRedisCacheConfigurationWithTtl(Integer minutes) {
        // 配置序列化
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        FastJson2JsonRedisSerializer jackson2JsonRedisSerializer = new FastJson2JsonRedisSerializer(Object.class);
        RedisCacheConfiguration redisCacheConfiguration = config.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .entryTtl(Duration.ofMinutes(minutes))
                .disableCachingNullValues()
                ;
        return redisCacheConfiguration;
    }
}

