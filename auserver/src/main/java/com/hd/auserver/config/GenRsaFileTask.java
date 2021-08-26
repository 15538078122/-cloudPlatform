package com.hd.auserver.config;

import com.hd.common.utils.RSAEncrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.security.interfaces.RSAPrivateKey;
import java.time.LocalDateTime;

/**
 * @Author: liwei
 * @Description:
 */
@Configuration
@EnableScheduling
public class GenRsaFileTask {

    @Value("${config.RSA_FILE_PATH}")
    String  RSA_FILE_PATH;

    public  static  String rsaPublicKey;
    public  static  RSAPrivateKey rsaPrivateKey;

    @PostConstruct
    public  void  init(){
        initialRsaKey();
    }
    //30分钟1次
    @Scheduled(cron = "0 0/30 * * * ?")
    private void configureTasks(){
        System.err.println("执行静态定时任务时间: "+RSA_FILE_PATH + LocalDateTime.now());
        initialRsaKey();
    }

    private void   initialRsaKey(){
        RSAEncrypt.genKeyPair(RSA_FILE_PATH);
        try {
            rsaPublicKey=RSAEncrypt.loadPublicKeyByFile(RSA_FILE_PATH);
            rsaPrivateKey=RSAEncrypt.loadPrivateKeyByStr(RSAEncrypt.loadPrivateKeyByFile(RSA_FILE_PATH));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}