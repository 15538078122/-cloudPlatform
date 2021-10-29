package com.hd.gateway.conf;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class DealGatewayFilterFactory extends AbstractGatewayFilterFactory<DealGatewayFilterFactory.Config>
{
    /**
     * 注入config类型，用于参数解析
     */
    public DealGatewayFilterFactory(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config)
    {
        return new DealGatewayFilter();
    }
    public static class Config {

        private String initValue;

        public String getInitValue() {
            return initValue;
        }
        public void setInitValue(String initValue) {
            this.initValue=initValue;
        }
    }
}
