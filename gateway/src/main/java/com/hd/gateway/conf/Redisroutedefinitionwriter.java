package com.hd.gateway.conf;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: liwei
 * @Description:
 */
@Component
@Slf4j
public class Redisroutedefinitionwriter implements RouteDefinitionRepository {

    @Autowired
    MicroServiceConfig microServiceConfig;

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        Map<String, String> microServices = microServiceConfig.getMicroServices();
        List<RouteDefinition> definitionlist = new ArrayList<>();
        String routeId="microattach";
        String uriPath="lb://microsys";
        String predicatePath="/microattach/**";
        int timeout=30000;
        String fallbackcmd="fallbackcmd60s";
        for (Map.Entry<String, String> item : microServices.entrySet()) {
            routeId = item.getKey();
            String[] values = item.getValue().split(",");
            uriPath = values[0];
            predicatePath = values[1];
            timeout = Integer.parseInt(values[2]);
            fallbackcmd = values[3];

            RouteDefinition routeDefinition=new RouteDefinition();
            routeDefinition.setId(routeId);
            URI uri = UriComponentsBuilder.fromUriString(uriPath).build().toUri();
            routeDefinition.setUri(uri);
            List<PredicateDefinition> predicates=new ArrayList<>();
            PredicateDefinition predicateDefinition=new PredicateDefinition();
            predicateDefinition.setName("Path");
            predicateDefinition.addArg("text",predicatePath);
            //_genkey_0 -> 1
            predicates.add(predicateDefinition);
            routeDefinition.setPredicates(predicates);

            List<FilterDefinition> filterDefinitions=new ArrayList<>();
            FilterDefinition filterDefinition=new FilterDefinition();
            filterDefinition.setName("StripPrefix");
            filterDefinition.addArg("parts","1");
            filterDefinitions.add(filterDefinition);

            filterDefinition=new FilterDefinition();
            filterDefinition.setName("Deal");
            filterDefinition.addArg("initValue","testparas");
            filterDefinitions.add(filterDefinition);

            filterDefinition=new FilterDefinition();
            filterDefinition.setName("Hystrix");
            filterDefinition.addArg("name",fallbackcmd);
            filterDefinition.addArg("fallbackUri","forward:/defaultfallback");
            filterDefinitions.add(filterDefinition);

            filterDefinition=new FilterDefinition();
            filterDefinition.setName("Retry");
            filterDefinition.addArg("retries","1");
            filterDefinition.addArg("series.0","SERVER_ERROR");
            filterDefinition.addArg("methods.0","GET");
            filterDefinitions.add(filterDefinition);
            routeDefinition.setFilters(filterDefinitions);

            Map<String,Object> metaMap=new HashMap<>();
            metaMap.put("connect-timeout",2000);
            metaMap.put("response-timeout",timeout);
            routeDefinition.setMetadata(metaMap);

            definitionlist.add(routeDefinition);
        }
        log.debug("redis 中路由定义条数： {}， {}", definitionlist.size(), definitionlist);
        return Flux.fromIterable(definitionlist);
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return route.flatMap(r -> {
            //routedefinitionvo vo = new routedefinitionvo();
            //beanutils.copyproperties(r, vo);
            log.info("保存路由信息", JSON.toJSONString(r.getMetadata()));
            //redistemplate.opsforhash().put(commonconstant.route_key, r.getid(), vo);
            return Mono.empty();
        });
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        routeId.subscribe(id -> {
            log.info("删除路由信息");
            //redistemplate.opsforhash().delete(commonconstant.route_key, id);
        });
        return Mono.empty();
    }
}
