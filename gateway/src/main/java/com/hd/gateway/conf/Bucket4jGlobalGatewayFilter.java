package com.hd.gateway.conf;

/**
 * @Author: liwei
 */
import com.hd.common.RetResult;
import com.hd.gateway.utils.ResponseUtil;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class Bucket4jGlobalGatewayFilter implements GlobalFilter, Ordered
{
    private static final Map<String, Bucket> BUCKET_CACHE = new ConcurrentHashMap<>();

    private Bucket createNewIpBucket()
    {
        //桶的最大容量，即能装载 Token 的最大数量
        int capacity = 100;
        //每次 Token 补充量
        int refillTokens = 10;
        //补充 Token 的时间间隔
        Duration duration = Duration.ofSeconds(1);
        Refill refill = Refill.greedy(refillTokens, duration);
        Bandwidth limit = Bandwidth.classic(capacity, refill);
        return Bucket4j.builder().addLimit(limit).build();
    }
    private Bucket createNewMicroSysServiceBucket()
    {
        //桶的最大容量，即能装载 Token 的最大数量
        int capacity = 2000;
        //每次 Token 补充量
        int refillTokens = 100;
        //补充 Token 的时间间隔
        Duration duration = Duration.ofSeconds(1);
        Refill refill = Refill.greedy(refillTokens, duration);
        Bandwidth limit = Bandwidth.classic(capacity, refill);
        return Bucket4j.builder().addLimit(limit).build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        String ip = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
        Bucket bucket1 = BUCKET_CACHE.computeIfAbsent(ip, k -> createNewIpBucket());
        log.debug("IP: " + ip + "，has Tokens: " + bucket1.getAvailableTokens());
        Boolean consumeSuccess=bucket1.tryConsume(1);
        if(consumeSuccess){
            String uri=exchange.getRequest().getPath().value();
            //url第一个分段一遍用作服务名识别
            String microName = uri.substring(0,uri.indexOf("/",1));
            if(microName.compareTo("/microsys")==0){
                Bucket bucket2 = BUCKET_CACHE.computeIfAbsent(microName, k -> createNewMicroSysServiceBucket());
                log.debug("service: " + microName + "，has Tokens: " + bucket2.getAvailableTokens());
                consumeSuccess=bucket2.tryConsume(1);
            }
        }

        if (consumeSuccess)
        {
            return chain.filter(exchange);
        }
        else
        {
            return ResponseUtil.makeJsonResponse(exchange.getResponse(),
                    new RetResult(HttpStatus.TOO_MANY_REQUESTS.value(),"请求超限",false)
                    );
        }
    }

    @Override
    public int getOrder()
    {
        return Ordered.HIGHEST_PRECEDENCE+100;
    }
}