package com.hd.auservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages={"com.hd.auservice","com.hd.common.conf"})
@EnableFeignClients(basePackages = "com.hd.auservice")
@EnableHystrix
@EnableDiscoveryClient
@ServletComponentScan(basePackages = "com.hd.auservice.conf")
@EnableSwagger2
//@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {com.hd.auc.conf.RetResultAdvice2.class}))
public class AuServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuServiceApplication.class, args);
	}
}
