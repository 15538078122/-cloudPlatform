package com.hd.gateway.conf;

import com.alibaba.fastjson.JSON;
import com.hd.common.model.TokenInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Author: liwei
 */
@Slf4j
public class DealGatewayFilter implements GatewayFilter, Ordered
{
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        //调整header的参数
        String tokenInfoJson = exchange.getRequest().getHeaders().getFirst("token-info");
        TokenInfo tokenInfo= JSON.parseObject(tokenInfoJson,TokenInfo.class);
        log.debug("tokenInfo:"+JSON.toJSONString(tokenInfo));
        return chain.filter(exchange);
    }

    @Override
    public int getOrder()
    {
        return Ordered.LOWEST_PRECEDENCE - 1000;
    }


}