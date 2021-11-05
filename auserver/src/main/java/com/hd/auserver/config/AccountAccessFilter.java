package com.hd.auserver.config;

import com.hd.common.utils.RSAEncrypt;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: liwei
 * @Description:
 */
@Slf4j
@WebFilter(filterName = "accountAccessFilter",urlPatterns = {"/account/*","/license"})
@Order(Ordered.HIGHEST_PRECEDENCE+100)
public class AccountAccessFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    @Value("${config.USER_OP_IDENTIFICATION}")
    String  USER_OP_IDENTIFICATION;

    @Autowired
    TokenConfig tokenConfig;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        //TODO: 临时屏蔽账号访问检查
//                if(true){
//                    chain.doFilter(request, response);
//                    return;
//                }
        String userOpIdentificationEncode = ((HttpServletRequest) request).getHeader("USER_OP_IDENTIFICATION");
        if(userOpIdentificationEncode==null){
            throw new RuntimeException("无权限！");
        }
        userOpIdentificationEncode = java.net.URLDecoder.decode(userOpIdentificationEncode, "UTF-8");
        byte[] res = new byte[0];
        try {
            res = RSAEncrypt.decrypt((RSAPrivateKey) tokenConfig.rsaPrivateKey, Base64.decode(userOpIdentificationEncode));
        } catch (Exception e) {
            throw new RuntimeException("无权限！");
        }
        String userOpIdentification=new String(res);
        String requestTimeStr=userOpIdentification.substring(USER_OP_IDENTIFICATION.length());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date requestTime = sdf.parse(requestTimeStr);
            Long mSec = System.currentTimeMillis() - requestTime.getTime();
            //30秒
            if (mSec > (30000 * 1000)) {
                throw new ServletException("无权限！");
            }
        } catch (ParseException e) {
            throw new ServletException("无权限！");
        }
        userOpIdentification = userOpIdentification.substring(0,USER_OP_IDENTIFICATION.length());

        if(userOpIdentification==null||userOpIdentification.compareTo(USER_OP_IDENTIFICATION)!=0){
            throw new ServletException("无权限！");
        }
        chain.doFilter(request, response);
    }

}
