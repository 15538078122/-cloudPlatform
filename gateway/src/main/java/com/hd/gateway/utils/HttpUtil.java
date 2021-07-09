package com.hd.gateway.utils;

import com.hd.common.RetResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public  class HttpUtil {
    static RestTemplate restTemplate;
    static {
        SimpleClientHttpRequestFactory requestFactory =
                new SimpleClientHttpRequestFactory();
        //读取超时
        requestFactory.setReadTimeout(5000);
        //连接超时
        requestFactory.setConnectTimeout(5000);
        restTemplate = new RestTemplate(requestFactory);
    }


    public static RetResult httpGet(String applicationName, Map<String, String> params){

        return restTemplate.getForObject("http://" + applicationName, RetResult.class,params);
    }

    public static RetResult httpPost(String applicationName, Map<String, String> params){
        Map<String, Object> hashMap = new HashMap<String, Object>();
        return restTemplate.postForObject("http://" + applicationName,hashMap,RetResult.class,params);
    }

}
