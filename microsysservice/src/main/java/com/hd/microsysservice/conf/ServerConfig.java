package com.hd.microsysservice.conf;

import com.hd.common.utils.NetUtil;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @Author: liwei
 * @Description:
 */
@Component
public class ServerConfig implements ApplicationListener<WebServerInitializedEvent> {
    public int getServerPort() {
        return serverPort;
    }

    private int serverPort;

    public String getUrl() {
//        InetAddress address = null;
//        try {
//            address = InetAddress.getLocalHost();
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//        return "http://"+address.getHostAddress()+":"+this.serverPort;
        return "http://"+ NetUtil.getNoDefaultIpAddr()+":"+this.serverPort;
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        serverPort = event.getWebServer().getPort();
    }
}
