package com.hd.micromonitorservice.conf;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: liwei
 * @Description:
 */
public class MyDispatcherServlet extends DispatcherServlet {
    @Override
    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception{

        super.doDispatch(request,response);
    }
    public MyDispatcherServlet(WebApplicationContext webApplicationContext) {
        super(webApplicationContext);
    }
}
