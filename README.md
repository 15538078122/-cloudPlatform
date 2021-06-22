# 介绍
1、认证OATH2.0，4种模式.
2、jwt rsa 

3、更简洁的jwt client方式 

4、网关gateway，认证鉴权限流负载均衡、请求监视、retry 

5、独立的url鉴权服务 

6、更具独立性，只专注于业务实现。通过设计便于业务数据读写分离

7、各个子系统模块均可根据需求，调整部署的实例数。 

8、数据库主从、连接池、数据缓存、消息队列、运维监视（完善中） 

9、nacos 服务发现


# 软件架构

![image](https://user-images.githubusercontent.com/83743182/122862067-2287bf00-d353-11eb-9c0d-2dccbc40a8df.png)
# 安装
自行安装nacos，网上很多。新建空project，导入各个module，如下：共6个
![image](https://user-images.githubusercontent.com/83743182/122862149-464b0500-d353-11eb-85fb-cfe306757c96.png)
global libraries里添加上libs目录下的common-0.0.1-SNAPSHOT.jar, 这是已经package的common项目
![image](https://user-images.githubusercontent.com/83743182/122862182-5367f400-d353-11eb-959e-387ad6dfc322.png)
分别配置上正确的 cloud discovery 地址：
![image](https://user-images.githubusercontent.com/83743182/122862198-5e228900-d353-11eb-853d-9c5ad2a62da5.png)
注意配置中心，可以把active:online,改成本地的。注意file-extension: yaml 要和nacos配置对应。 
![image](https://user-images.githubusercontent.com/83743182/122862260-6e3a6880-d353-11eb-86f3-2158f21192cc.png)
启动五个应用
![image](https://user-images.githubusercontent.com/83743182/122862291-7a262a80-d353-11eb-8193-5701d8cae66f.png)
![image](https://user-images.githubusercontent.com/83743182/122862304-801c0b80-d353-11eb-9506-2f8b5fd2696e.png)
小技巧： 显示rundashboard，方便管理启动多个应用。 idea目录下找到workspace.xml, 增加配置：
![image](https://user-images.githubusercontent.com/83743182/122862342-8f9b5480-d353-11eb-9e8d-b1d6691490f8.png)
也可以package后，启动都给微服务进行测试：例如命令： java -jar microservice-0.0.1-SNAPSHOT.jar --server.port=8085
指定不同的port，启动多个
# 脚本使用说明
postman 导入gateway&oauth2&microservice-testing.postman_collection.json
![image](https://user-images.githubusercontent.com/83743182/122862436-b78ab800-d353-11eb-97ed-5224c7cc0f37.png)
注意：授权码模式的两个url需要放到浏览器中访问。根据postman中的请求列表，开始你的访问吧
# 系统几个关键坑介绍：
有时间补。。。。。。 先列下：

1、mvn问题，gateway和spring boot 的版本注意匹配问题。 oauth和spring boot版本匹配问题；不匹配可能会有异常发生。
cloud最好选择dependencyManagement方式，避免自己指定version时发送不兼容。

2、gateway只引用了security-jwt；没有引用oauth2-autoconfigure； 引入会异常，只借用了解析jwt的JwtHelper class而已，也是看源码揣摩的，开始是自己写jwt工具比较麻烦。

3、auserver里面使用自定义jwt和自定义登录以及授权页面时，scope失效，不能正常传递；这里重写了DefaultOAuth2RequestFactory ，处理授权时scope没有正确处理的问题。

4、重写MyBCryptPasswordEncoder，处理前端传递密码解密，安全问题。

注意里面的这些todo，实际应用都需要自己修改的地方：
![image](https://user-images.githubusercontent.com/83743182/122862549-d6894a00-d353-11eb-9053-1084ba23783b.png)
token验证后，uri的访问验证，采用了访问auservice方式。注意这里使用了RestTemplate直接访问的；

6、hystrix 熔断 timeout和readtimeout，注意区别；熔断未超时，读取超时，会采用retry策略重试。

7、网关里面ribbon的策略配置有效，其它如timeout，MaxAutoRetries配置无效，这些配置在gateway里利用metadata和filter实现，

![image](https://user-images.githubusercontent.com/83743182/122862570-e4d76600-d353-11eb-8d9f-7e3f34ac38e7.png)
8、gateway filter设置顺序，我设置的是：限流Bucket4jGlobalGatewayFilter-》TimeCostGlobalGatewayFilter-》CheckAuthGlobalGatewayFilter（认证和鉴权） DealGatewayFilter用来测试

9、反漏洞限流模式很好的处理突变量访问问题，处理限流、熔断来保证系统可用性；一般系统更多的性能问题还是卡在自身的数据处理上，逻辑不合理造成耗时，数据结构不合理、数据库相关处理，数据量、索引，查询sql效率等。

10、数据返回，做成了一个common基础包，供各个服务项目引用，比较统一，注意启动scan配置@SpringBootApplication(scanBasePackages={"com.hd.microservice","com.hd.common.conf"})，加上common.conf包的扫描。
