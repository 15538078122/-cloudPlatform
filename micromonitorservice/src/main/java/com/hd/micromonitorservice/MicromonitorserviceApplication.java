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


	/**
	 * 修改dispatch拦截路径
	 * @param dispatcherServlet
	 * @return
	 */
//	@Bean
//	public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {
//		ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet);
//		registration.getUrlMappings().clear();
//		registration.addUrlMappings("/*");
//		//registration.addUrlMappings("*.json");
//		return registration;
//	}
	//@Bean
//	public ServletRegistrationBean restServlet(){
//		//注解扫描上下文
//		AnnotationConfigWebApplicationContext applicationContext
//				= new AnnotationConfigWebApplicationContext();
//		//base package
//		//applicationContext.scan("com.jerryl.rest");
//		//通过构造函数指定dispatcherServlet的上下文
//		DispatcherServlet rest_dispatcherServlet
//				= new MyDispatcherServlet(applicationContext);
//
//		//用ServletRegistrationBean包装servlet
//		ServletRegistrationBean registrationBean
//				= new ServletRegistrationBean(rest_dispatcherServlet);
//		registrationBean.setLoadOnStartup(1);
//		//指定urlmapping
//		registrationBean.addUrlMappings("/xxxxxx/*");
//		//指定name，如果不指定默认为dispatcherServlet
//		registrationBean.setName("rest");
//		return registrationBean;
//	}
}
