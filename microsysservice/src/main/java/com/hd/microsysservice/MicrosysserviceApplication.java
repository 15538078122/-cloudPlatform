package com.hd.microsysservice;

import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
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

@SpringBootApplication(scanBasePackages={"com.hd.microsysservice","com.hd.common.conf"})
@EnableFeignClients(basePackages = "com.hd.microsysservice")
@EnableHystrix
@EnableDiscoveryClient
@ServletComponentScan(basePackages = "com.hd.microsysservice.conf")
@MapperScan("com.hd.microsysservice.mapper")
//@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {com.hd.auc.conf.RetResultAdvice2.class}))
@EnableSwagger2
@EnableTransactionManagement
@EnableCaching
@EnableAutoDataSourceProxy
public class MicrosysserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicrosysserviceApplication.class, args);
	}
}
