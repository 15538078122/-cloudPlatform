package com.hd.gateway.conf;

import com.hd.common.utils.MongoLogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

//全局过滤器，使用配置类形式，直接构造bean，使用注解完成Ordered接口功能,统计接口调用时间
//@Configuration
//public class TimeCostGlobalGatewayFilter
//{
//    @Bean
//    @Order(Ordered.HIGHEST_PRECEDENCE+50)
//    public GlobalFilter elapsedGlobalFilter()
//    {
//        return (exchange, chain) -> {
//            //调用请求之前统计时间
//            Long startTime = System.currentTimeMillis();
//            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
//                //调用请求之后统计时间
//                Long endTime = System.currentTimeMillis();
//                System.out.println(
//                        exchange.getRequest().getURI().getRawPath() + ", cost time : " + (endTime - startTime) + "ms");
//            }));
//        };
//    }
//}
@Component
@Slf4j
public class TimeCostGlobalGatewayFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Long startTime = System.currentTimeMillis();
        //System.out.println("begin call time: " + new Date());
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            //调用请求之后统计时间
            Long endTime = System.currentTimeMillis();
            log.debug(
                    exchange.getRequest().getURI().getRawPath() + ", cost time : " + (endTime - startTime) + "ms");
            MongoLogUtil.logCostTime(exchange.getRequest().getURI().getRawPath(),(endTime - startTime));
        }));
    }
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 200;
    }
}
