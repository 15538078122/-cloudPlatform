package com.hd.gateway.utils;

import com.alibaba.fastjson.JSONObject;
import com.hd.gateway.model.RetResult;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;


public final class ResponseUtil {
    public static Mono<Void>  makeJsonResponse(ServerHttpResponse response, RetResult retResult){
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        String jsonStr = JSONObject.toJSONString(retResult);
        byte[] bits = jsonStr.getBytes(StandardCharsets.UTF_8);
        //把字节数据转换成一个DataBuffer
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        return response.writeWith(Mono.just(buffer));
    }
}
