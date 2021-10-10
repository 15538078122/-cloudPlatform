package com.hd.common.utils;

import com.hd.common.RetResult;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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
        restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
    }


    public static RetResult httpGet(String url, MultiValueMap<String, String> params){
        return restTemplate.getForObject(url, RetResult.class,params);
    }


    public static RetResult httpPost(String url, MultiValueMap<String, String> params){
        //Map<String, Object> hashMap = new HashMap<String, Object>();
        //return restTemplate.postForObject(url,params,RetResult.class,params);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(params, headers);
        return restTemplate.exchange(url, HttpMethod.POST,requestEntity,RetResult.class).getBody();
    }
    public static RetResult httpPostWithIdenHeader(String url, MultiValueMap<String, String> params, String USER_OP_IDENTIFICATION){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("USER_OP_IDENTIFICATION",USER_OP_IDENTIFICATION);

        HttpEntity<Object> requestEntity = new HttpEntity<Object>(params, headers);
        return restTemplate.exchange(url, HttpMethod.POST,requestEntity,RetResult.class).getBody();
    }
    public static RetResult httpPutWithIdenHeader(String url, MultiValueMap<String, String> params, String USER_OP_IDENTIFICATION){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("USER_OP_IDENTIFICATION",USER_OP_IDENTIFICATION);

        HttpEntity<Object> requestEntity = new HttpEntity<Object>(params, headers);
        return restTemplate.exchange(url, HttpMethod.PUT,requestEntity,RetResult.class).getBody();
    }
    public static RetResult httpDelWithIdenHeader(String url, MultiValueMap<String, String> params, String USER_OP_IDENTIFICATION){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("USER_OP_IDENTIFICATION",USER_OP_IDENTIFICATION);

        HttpEntity<Object> requestEntity = new HttpEntity<Object>(params, headers);
        return restTemplate.exchange(url, HttpMethod.DELETE,requestEntity,RetResult.class).getBody();
    }
    public static RetResult httpGetWithIdenHeader(String url, MultiValueMap<String, String> params, String USER_OP_IDENTIFICATION){
        HttpHeaders headers = new HttpHeaders();
        headers.add("USER_OP_IDENTIFICATION",USER_OP_IDENTIFICATION);
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
        return restTemplate.exchange(url+"?enterprise="+params.getFirst("enterprise")+"&account="+params.getFirst("account"),
                HttpMethod.GET,requestEntity,RetResult.class).getBody();
    }
}
