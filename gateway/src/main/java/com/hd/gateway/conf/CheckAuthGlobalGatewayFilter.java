package com.hd.gateway.conf;

/**
 * @Author: liwei
 */

import com.alibaba.fastjson.JSON;
import com.hd.gateway.GatewayApplication;
import com.hd.gateway.model.RetResult;
import com.hd.gateway.model.TokenInfo;
import com.hd.gateway.utils.HttpUtil;
import com.hd.gateway.utils.JwtUtils;
import com.hd.gateway.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;


//全局过滤器，实现GlobalFilter接口，和Ordered接口即可。
@Component
@Slf4j
public class CheckAuthGlobalGatewayFilter implements GlobalFilter, Ordered {
    @Autowired
    JwtUtils jwtUtils;

    @Value("${config.auth-uri}")
    String authUri;

    //TODO: 通过网关访问swagger时，关闭权限检查，开发阶段使用
    @Value("${config.check-permission}")
    boolean checkPermission=true;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if(!checkPermission){
            return chain.filter(exchange);
        }
        //TODO: PERMISSION 判断
        //获取header的参数
        String tokenInfoJson = exchange.getRequest().getHeaders().getFirst("token-info");
        TokenInfo tokenInfo= JSON.parseObject(tokenInfoJson,TokenInfo.class);

        String user = tokenInfo.getAccount();
        //HttpUtil callService = GatewayApplication.applicationContext.getBean(HttpUtil.class);

        RetResult retResult;
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("account", tokenInfo.getAccount());
            params.put("scopes", tokenInfo.getScopes());
            params.put("uri", tokenInfo.getUri());
            params.put("method", tokenInfo.getMethod());
            params.put("companyCode", tokenInfo.getCompanyCode());
            //retResult=new RetResult(0,"",true);
            retResult= HttpUtil.httpPost(authUri+"/authbridge?account={account}&scopes={scopes}&uri={uri}&method={method}&companyCode={companyCode}",params);
        } catch (Exception e) {
            retResult = new RetResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), false);
        }

        boolean permitted=false;
        if(retResult.getData()!=null){
            permitted = (Boolean) retResult.getData();
        }

        if (!permitted)  {
            //TODO: 记录拒绝访问日志
            retResult = new RetResult(HttpStatus.UNAUTHORIZED.value(), "授权异常!", false);
            return ResponseUtil.makeJsonResponse(exchange.getResponse(), retResult);
        }
        //TODO: 记录访问日志
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 301;
    }
}