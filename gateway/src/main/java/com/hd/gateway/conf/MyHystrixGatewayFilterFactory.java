package com.hd.gateway.conf;

/**
 * @Author: liwei
 * @Description:
 */

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixObservableCommand;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.TimeoutException;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.DispatcherHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import rx.Observable;
import rx.RxReactiveStreams;
import rx.Subscription;

import java.net.URI;
import java.util.*;
import java.util.function.Function;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.containsEncodedParts;

/**
 * Copyright 2013-2017 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Apache(源码修改 HystrixGatewayFilterFactory)
 * @ClassName MyHystrixGatewayFilterFactory
 * @Description 自定义的HystrixGatewayFilterFactory主要用来调整特殊接口的容断时间
 * @Modify TY
 * @Date 17:20 2019-07-11
 * @Version 1.0
 **/
@Component
public class MyHystrixGatewayFilterFactory extends AbstractGatewayFilterFactory<MyHystrixGatewayFilterFactory.Config> {

    private static final String NAME = "MyHystrix";

    private final ObjectProvider<DispatcherHandler> dispatcherHandler;

    public MyHystrixGatewayFilterFactory(ObjectProvider<DispatcherHandler> dispatcherHandler) {
        super(Config.class);
        this.dispatcherHandler = dispatcherHandler;
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.singletonList(NAME_KEY);
    }
    PathMatcher pathMatcher=new AntPathMatcher();

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getPath().pathWithinApplication().value();

            Map<String, Integer> timeoutMap = config.getTimeout();
            Integer timeout = null;
            if (timeoutMap != null) {
                //timeout = timeoutMap.get(path);
                Set<Map.Entry<String, Integer>> set = timeoutMap.entrySet();
                for (Map.Entry<String, Integer> entry : set) {
                    String key = entry.getKey();
                    Integer value = entry.getValue();
                    boolean match = pathMatcher.match(key, path);
                    if(match){
                        timeout=value;
                    }
                }
            }

            MyRouteHystrixCommand command;
            if (timeout == null) {
                //没有定义时间的接口将使用配置的default时间
                command = new MyRouteHystrixCommand(config.getFallbackUri(), exchange, chain, path);
            } else {
                //有配置时间的接口将使用配置的时间
                command = new MyRouteHystrixCommand(config.getFallbackUri(), exchange, chain, timeout, path);
            }

            return Mono.create(s -> {
                Subscription sub = (Subscription) command.toObservable().subscribe(s::success, s::error, s::success);
                s.onCancel(sub::unsubscribe);
            }).onErrorResume((Function<Throwable, Mono<Void>>) throwable -> {
                if (throwable instanceof HystrixRuntimeException) {
                    HystrixRuntimeException e = (HystrixRuntimeException) throwable;
                    HystrixRuntimeException.FailureType failureType = e.getFailureType();
                    switch (failureType) {
                        case TIMEOUT:
                            return Mono.error(new TimeoutException());
                        case COMMAND_EXCEPTION: {
                            Throwable cause = e.getCause();
                            if (cause instanceof ResponseStatusException || AnnotatedElementUtils
                                    .findMergedAnnotation(cause.getClass(), ResponseStatus.class) != null) {
                                return Mono.error(cause);
                            }
                        }
                        default:
                            break;
                    }
                }
                return Mono.error(throwable);
            }).then();
        };
    }

    @Override
    public String name() {
        return NAME;
    }

    private class MyRouteHystrixCommand extends HystrixObservableCommand<Void> {

        private final URI fallbackUri;
        private final ServerWebExchange exchange;
        private final GatewayFilterChain chain;

        public MyRouteHystrixCommand(URI fallbackUri, ServerWebExchange exchange, GatewayFilterChain chain,
                                     String key) {
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(key))
                    .andCommandKey(HystrixCommandKey.Factory.asKey(key)));
            this.fallbackUri = fallbackUri;
            this.exchange = exchange;
            this.chain = chain;

        }

        public MyRouteHystrixCommand(URI fallbackUri, ServerWebExchange exchange, GatewayFilterChain chain,
                                     int timeout,
                                     String key) {
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(key))
                    .andCommandKey(HystrixCommandKey.Factory.asKey(key))
                    .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionIsolationThreadTimeoutInMilliseconds(timeout)));
            this.fallbackUri = fallbackUri;
            this.exchange = exchange;
            this.chain = chain;

        }

        @Override
        protected Observable<Void> construct() {
            return RxReactiveStreams.toObservable(this.chain.filter(exchange));
        }

        @Override
        protected Observable<Void> resumeWithFallback() {
            if (null == fallbackUri) {
                return super.resumeWithFallback();
            }
            URI uri = exchange.getRequest().getURI();
            boolean encoded = containsEncodedParts(uri);
            URI requestUrl = UriComponentsBuilder.fromUri(uri)
                    .host(null)
                    .port(null)
                    .uri(this.fallbackUri)
                    .build(encoded)
                    .toUri();
            exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, requestUrl);

            ServerHttpRequest request = this.exchange.getRequest().mutate().uri(requestUrl).build();
            ServerWebExchange mutated = exchange.mutate().request(request).build();
            DispatcherHandler dispatcherHandler = MyHystrixGatewayFilterFactory.this.dispatcherHandler.getIfAvailable();
            return RxReactiveStreams.toObservable(dispatcherHandler.handle(mutated));
        }
    }

    public static class Config {

        private String id;
        private URI fallbackUri;
        /**
         * url -> timeout ms
         */
        private Map<String, Integer> timeout;

        public String getId() {
            return id;
        }

        public Config setId(String id) {
            this.id = id;
            return this;
        }

        public URI getFallbackUri() {
            return fallbackUri;
        }

        public Config setFallbackUri(URI fallbackUri) {
            if (fallbackUri != null && !"forward".equals(fallbackUri.getScheme())) {
                throw new IllegalArgumentException("Hystrix Filter currently only supports 'forward' URIs, found " + fallbackUri);
            }
            this.fallbackUri = fallbackUri;
            return this;
        }

        public Map<String, Integer> getTimeout() {
            return timeout;
        }

        public Config setTimeout(Map<String, Integer> timeout) {
            //YAML解析的时候MAP的KEY不支持'/'，这里只能用'-'替代
            Map<String, Integer> tempTimeout = new HashMap<>(timeout.size());
            for (String key : timeout.keySet()) {
                Integer value = timeout.get(key);
                key = key.replace(".1", "{");
                key = key.replace(".2", "}");
                key = key.replace("-", "/");
                if (!key.startsWith("/")) {
                    key = "/" + key;
                }
                tempTimeout.put(key, value);
            }
            this.timeout = tempTimeout;
            return this;
        }
    }
}
