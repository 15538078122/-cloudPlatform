package com.hd.gateway.conf;

/**
 * @Author: liwei
 */

import com.alibaba.fastjson.JSON;
import com.hd.common.RetResult;
import com.hd.common.model.TokenInfo;
import com.hd.gateway.utils.JwtUtils;
import com.hd.gateway.utils.ResponseUtil;
import io.lettuce.core.output.BooleanListOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


//全局过滤器，实现GlobalFilter接口，和Ordered接口即可。
@Component
@Slf4j
public class CheckTokenGlobalGatewayFilter implements GlobalFilter, Ordered {
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RedisTemplate redisTemplate;

    @Value("${config.session-timeout}")
    int sessionTimeout;

    //TODO: 通过网关访问swagger时，关闭权限检查，开发阶段使用
    @Value("${config.check-token}")
    boolean checkPermission=true;

    public static final ThreadLocal<Boolean> toSaveToken = new ThreadLocal<>();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String uri=exchange.getRequest().getPath().value();
        String microName=uri.substring(1,uri.indexOf("/",1));
        if(!checkPermission||microName.compareTo("auserver")==0){
            return chain.filter(exchange);
        }
        List<String> authorization = exchange.getRequest().getHeaders().get("Authorization");
        if(authorization==null || authorization.size()==0){
            return ResponseUtil.makeJsonResponse(exchange.getResponse(),
                    new RetResult(HttpStatus.UNAUTHORIZED.value(), "没有登录令牌!", null));
        }
        String bearerTk = authorization.get(0);
        TokenInfo tokenInfo;
        try {
            String tokenStr = bearerTk.replace("Bearer ", "");
            //先判断本地缓存是否存在此token
             Object redisObj = redisTemplate.opsForValue().get(String.format("edgeOut:%s",tokenStr));
            Boolean tokenExist=(redisObj!=null);
            if(tokenExist){
                Boolean edgeOut=(Boolean)  redisTemplate.opsForValue().get(String.format("edgeOut:%s",tokenStr));
                //检查是否已被冲掉
                if(edgeOut){
                    return ResponseUtil.makeJsonResponse(exchange.getResponse(),
                            new RetResult(HttpStatus.UNAUTHORIZED.value(), "该账号已在其他设备登录!", null));
                }
                else {
                    //更新token tll
                    redisTemplate.expire(String.format("token:%s",tokenStr),sessionTimeout*60, TimeUnit.SECONDS);
                    redisTemplate.expire(String.format("edgeOut:%s",tokenStr),(sessionTimeout+5)*60,TimeUnit.SECONDS);
                    toSaveToken.set(false);
                    //从缓存获取tokeninfo
                    tokenInfo= JSON.parseObject(redisTemplate.opsForValue().get(String.format("token:%s",tokenStr)).toString(),TokenInfo.class);
                }
            }
            else {
                try {
                    tokenInfo = jwtUtils.decodeToken(tokenStr);
                    //url第一个分段一遍用作服务名识别，此处去掉
                    tokenInfo.setUri(uri.substring(uri.indexOf("/",1)));
                    tokenInfo.setMethod(exchange.getRequest().getMethodValue().toLowerCase());
                } catch (Exception e) {
                    e.printStackTrace();
                    return ResponseUtil.makeJsonResponse(exchange.getResponse(),
                            new RetResult(HttpStatus.UNAUTHORIZED.value(), "登录校验失败!", null));
                }
                //TODO: 根据应用，判断token超时
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Long mSec = System.currentTimeMillis() - sdf.parse(tokenInfo.getLoginTime()).getTime();
                //120分钟
                if (mSec > (60000 * sessionTimeout)) {
                    return ResponseUtil.makeJsonResponse(exchange.getResponse(),
                            new RetResult(HttpStatus.UNAUTHORIZED.value(), "登录token已过期!", null));
                }
                //判断是否有重复login
                String existKey = isRepeatLogin(tokenInfo);
                if(!existKey.isEmpty()){
                    //设置edgeout标识
                    //                    return ResponseUtil.makeJsonResponse(exchange.getResponse(),
                    //                            new RetResult(HttpStatus.UNAUTHORIZED.value(), "重复登录!", null));
                    log.debug("挤掉重复的登录:"+ tokenInfo.getAccount());
                    redisTemplate.opsForValue().set(String.format("edgeOut:%s",existKey.replace("token:","")),true, (sessionTimeout+5)*60,TimeUnit.SECONDS);
                    //删除缓存的tokeninfo，不需要了
                    redisTemplate.delete(existKey);
                }
                //保存token，先写to_save_token标识,在授权检查后保存，因为要调整tokeninfo中的centerid为业务系统的userid
                toSaveToken.set(true);
                //把认证中心的用户id，设置到authId上
                tokenInfo.setOauthId(tokenInfo.getId());
                tokenInfo.setId("");
                //                redisTemplate.opsForValue().set(String.format("token:%s",tokenStr),tokenInfo, sessionTimeout*60,TimeUnit.SECONDS);
                //                redisTemplate.opsForValue().set(String.format("edgeOut:%s",tokenStr),false,  (sessionTimeout+5)*60,TimeUnit.SECONDS);
            }
        } catch (ParseException e) {
            //e.printStackTrace();
            return ResponseUtil.makeJsonResponse(exchange.getResponse(),
                    new RetResult(HttpStatus.UNAUTHORIZED.value(), "登录错误!", null));
        }

        ServerHttpRequest request = exchange.getRequest().mutate().header("token-info", JSON.toJSONString(tokenInfo)).header("Authorization","")
                .header("Authorization",bearerTk).build();
        ServerWebExchange buildExchange = exchange.mutate().request(request).build();

        //TODO: 记录访问日志
        return chain.filter(buildExchange);
    }

    private String isRepeatLogin(TokenInfo tokenInfo) {
        Set<String> keys = redisTemplate.keys(String.format("token:%s", "*"));
        //List<TokenInfo> tokenInfoList = redisTemplate.opsForValue().multiGet(keys);
        String exist="";
        for (String key : keys)
        {
            TokenInfo item= JSON.parseObject(redisTemplate.opsForValue().get(key).toString(),TokenInfo.class);
            if(item.getEnterpriseId().compareTo(tokenInfo.getEnterpriseId())==0
                &&item.getAccount().compareTo(tokenInfo.getAccount())==0
                &&item.getDeviceType().compareTo(tokenInfo.getDeviceType())==0){
                Boolean edgeOut=(Boolean)  redisTemplate.opsForValue().get(String.format("edgeOut:%s",key.replace("token:","")));
                if(!edgeOut){
                    exist= key;
                    break;
                }
            }
        }
        return  exist;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 300;
    }
}