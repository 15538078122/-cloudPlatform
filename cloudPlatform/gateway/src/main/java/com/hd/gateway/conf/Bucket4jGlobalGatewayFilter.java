package com.hd.gateway.conf;

/**
 * @Author: liwei
 */
import com.hd.gateway.model.RetResult;
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
    //桶的最大容量，即能装载 Token 的最大数量
    int capacity = 1000;

    //每次 Token 补充量
    int refillTokens = 100;

    Duration duration = Duration.ofSeconds(1); //补充 Token 的时间间隔

    private static final Map<String, Bucket> BUCKET_CACHE = new ConcurrentHashMap<>();

    private Bucket createNewBucket()
    {
        Refill refill = Refill.greedy(refillTokens, duration);
        Bandwidth limit = Bandwidth.classic(capacity, refill);
        return Bucket4j.builder().addLimit(limit).build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        String ip = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
        Bucket bucket = BUCKET_CACHE.computeIfAbsent(ip, k -> createNewBucket());
        if(bucket.getAvailableTokens()>0) {
            log.info("IP: " + ip + "，has Tokens: " + bucket.getAvailableTokens());
            //System.out.println("IP: " + ip + "，has Tokens: " + bucket.getAvailableTokens());
        }

        if (bucket.tryConsume(1))
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