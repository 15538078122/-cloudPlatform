package com.hd.auserver.controller;

import com.hd.auserver.config.GenRsaFileTask;
import com.hd.auserver.config.MyBCryptPasswordEncoder;
import com.hd.auserver.config.TokenConfig;
import com.hd.auserver.utils.JwtUtils;
import com.hd.common.RetResponse;
import com.hd.common.RetResult;
import com.hd.common.utils.RSASignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPrivateKey;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author: liwei
 * @Description:
 */
@RestController
public class RsaPubkeyController {

    @Autowired
    TokenConfig tokenConfig;
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/rsapubkey")
    public RetResult rsapubkey() {
        return RetResponse.makeRsp(GenRsaFileTask.rsaPublicKey);
    }

    @PostMapping("/license")
    public  void  license(String machineCode, Long userCount,Long days,String enterpriseId, HttpServletResponse response) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String expDate="2099-01-01";
        if(days.compareTo(-1L)!=0){
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, Math.toIntExact(days));
            expDate = sdf.format(calendar.getTime());
        }

        //machineCode = LicenseUtil.getMachineCode();
        String sign = RSASignature.sign((RSAPrivateKey) tokenConfig.rsaPrivateKey, machineCode+userCount+expDate+enterpriseId);
        sign=String.format("%s%05d%s",expDate,userCount,sign);
        //Boolean res = RSASignature.doCheck(machineCode+userCount+expDate+enterpriseId,sign.substring(15,sign.length()),jwtUtils.rsaPublicKey);
        //String sf= JSON.toJSONString(sign);
        try(ServletOutputStream output = response.getOutputStream()) {
            //初始化response.
            response.reset();
            response.setBufferSize(20480);
            //response.setHeader("Content-type", "application/octet-stream;charset=UTF-8");
            String disposition = "attachment";
            String filename = "license.txt";
            response.setHeader("Content-Disposition", disposition + ";filename=" +
                    URLEncoder.encode(filename, StandardCharsets.UTF_8.name()));
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("ETag", URLEncoder.encode(filename, StandardCharsets.UTF_8.name()));
            response.setContentType("application/octet-stream;charset=UTF-8");
            Integer length=sign.length();
            response.setHeader("Content-Range", "bytes " + 0 + "-" + (length-1) + "/" + length);
            response.setHeader("Content-length", String.valueOf(length));
            response.setStatus(HttpServletResponse.SC_OK);
            output.write(sign.getBytes());
            output.flush();
            response.flushBuffer();
        }catch (Exception e) {
            throw new RuntimeException("授权失败：" + e.getMessage());
        }

    }

    @PostMapping("/pwdrsa")
    public  void  pwdrsa(String pwd, HttpServletResponse response) throws Exception {
        //machineCode = LicenseUtil.getMachineCode();
        String pwdrsa = ((MyBCryptPasswordEncoder)bCryptPasswordEncoder).RsaEncodePwd(pwd);
        //String sf= JSON.toJSONString(sign);
        try(ServletOutputStream output = response.getOutputStream()) {
            //初始化response.
            response.reset();
            response.setBufferSize(20480);
            //response.setHeader("Content-type", "application/octet-stream;charset=UTF-8");
            String disposition = "attachment";
            String filename = "pwdrsa.txt";
            response.setHeader("Content-Disposition", disposition + ";filename=" +
                    URLEncoder.encode(filename, StandardCharsets.UTF_8.name()));
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("ETag", URLEncoder.encode(filename, StandardCharsets.UTF_8.name()));
            response.setContentType("application/octet-stream;charset=UTF-8");
            Integer length=pwdrsa.length();
            response.setHeader("Content-Range", "bytes " + 0 + "-" + (length-1) + "/" + length);
            response.setHeader("Content-length", String.valueOf(length));
            response.setStatus(HttpServletResponse.SC_OK);
            output.write(pwdrsa.getBytes());
            output.flush();
            response.flushBuffer();
        }catch (Exception e) {
            throw new RuntimeException("加密rsa pwd：" + e.getMessage());
        }
    }

}
