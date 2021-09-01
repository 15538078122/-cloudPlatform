package com.hd.micromonitorservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages={"com.hd.micromonitorservice","com.hd.common.conf"})
@EnableFeignClients(basePackages = "com.hd.micromonitorservice")
@EnableHystrix
@EnableDiscoveryClient
@ServletComponentScan(basePackages = "com.hd.micromonitorservice.conf")
@MapperScan("com.hd.micromonitorservice.mapper")
//@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {com.hd.auc.conf.RetResultAdvice2.class}))
@EnableSwagger2
@EnableTransactionManagement
@EnableCaching
public class MicromonitorserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicromonitorserviceApplication.class, args);
	}
}
