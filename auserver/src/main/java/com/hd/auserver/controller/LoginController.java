package com.hd.auserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Author: liwei
 * @Date 2021-01-08
 */
@Controller
@Slf4j
public class LoginController {
    @Value("${server.port}")
    private String port;

    @GetMapping("/login")
    public String serve(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        //TODO: 自定义登录页面
        return "login";
    }

    @RequestMapping({"/","/index.html","/home.html"})
    public String test(Map<String, Object> model, HttpServletRequest request) {
        //TODO: 其它页面 都返回invalid页
        return "invalid";
    }
}
