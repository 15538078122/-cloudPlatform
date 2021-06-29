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
    @GetMapping("/public/{info}")
    public String publicResource(@PathVariable String info, @RequestParam String code) {
        return "Public information " + info+":"+code;
    }
    @GetMapping("/public/token")
    public String publicResource() {
        return "getToken";
    }


    @ResponseBody
    @GetMapping("/private/{info}")
    public String privateResource(@PathVariable String info) {
        return "Private information : " + info;
    }

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

}
