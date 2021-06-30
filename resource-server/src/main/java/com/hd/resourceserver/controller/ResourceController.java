package com.hd.resourceserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: liwei
 * @Date 2021-01-08
 */
@Controller
public class ResourceController {

    @ResponseBody
    @GetMapping("/private/read/{info}")
    public String privateRead(@PathVariable String info) {
        return "Private read : " + info;
    }

    @ResponseBody
    @GetMapping("/private/write/{info}")
    public String privateWrite(@PathVariable String info) {
        return "Private write : " + info;
    }

    @ResponseBody
    @GetMapping("/public/{info}")
    /**
     * 授权码返回页面
     */
    public String publicResource(@PathVariable String info, @RequestParam String code) {
        return "Public information " + info+":"+code;
    }

    /**
     * 获取token返回页面
     * @return
     */
    @GetMapping("/public/token")
    public String publicResource() {
        return "getToken";
    }

}
