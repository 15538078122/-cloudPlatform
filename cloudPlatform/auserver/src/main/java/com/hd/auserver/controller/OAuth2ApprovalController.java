package com.hd.auserver.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.Map;

@Controller
@SessionAttributes("authorizationRequest")
public class OAuth2ApprovalController {

    @RequestMapping("/oauth/confirm_access")
    public String getAccessConfirmation(Map<String, Object> model, HttpServletRequest request)
            throws Exception {
        model.put("test", "自定义scope设置");
        if (model.get("authorizationRequest") != null){
            AuthorizationRequest authorizationRequest=(AuthorizationRequest)model.get("authorizationRequest");
        }
        //TODO: 授权scope自定义页面
        return "approval";
    }
}
