# 介绍 多企业 多应用 基础平台
1、认证OATH2.0，4种模式.   AUTHORIZATION_CODE,GRANT_TYPE, REFRESH_TOKEN,GRANT_TYPE_PASSWORD,IMPLICIT
2、jwt rsa   
3、更简洁的jwt client方式   
4、网关gateway，认证鉴权限流负载均衡、请求监视、retry   
5、独立的url鉴权服务   
6、更具独立性，只专注于业务实现。通过设计便于业务数据读写分离  
7、各个子系统模块均可根据需求，调整部署的实例数。   
8、数据库主从、连接池、数据缓存、消息队列、运维监视（完善中）   
9、nacos 服务发现  mongodb存日志  
10、分布式锁  
11、rsa动态密钥  
12、文件分片上传和分片下载  
# 软件架构   
![1630457763(1)](https://user-images.githubusercontent.com/83743182/131594644-f7acabc8-c722-4b54-a717-d771850a2a37.jpg)
# 基础数据模型  
![image](https://user-images.githubusercontent.com/83743182/125550679-ddde7fae-defb-429f-ab37-ed5f5bd1844a.png)
# 基础url  
![image](https://user-images.githubusercontent.com/83743182/131476894-2fa250fd-3bbd-45d9-8756-13a5dd6576f0.png)
![image](https://user-images.githubusercontent.com/83743182/131476990-313fbbd9-fac1-4790-a906-a97f17927b02.png)
![image](https://user-images.githubusercontent.com/83743182/125554063-4947a8c2-bde2-4979-94a9-d6d8e639e45a.png)

# 安装
安装数据库：脚本user-center.sql  和 patrol.sql  
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

# ui
![image](https://user-images.githubusercontent.com/83743182/131477976-33134c78-ef67-48bd-a546-f3ce88c252cf.png)


# 系统若干技术问题介绍：
 先列下：

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
11、log写入mongo db，一定记得去掉spring-boot-devtools， 由于classloader不同造成UnsynchronizedAppenderBase获取 applicationContext失败，进而无法写入mongodb。   
12、auservice增加swagger2，注意  beforeBodyWrite的返回值处理，不能全部返回RetResult类型，会造成swagger页面访问失败
![image](https://user-images.githubusercontent.com/83743182/123385246-eb6e1380-d5c7-11eb-93bf-02cbb1bc2944.png)
13、增加uri 扫描类ApiUtils，备以后将uri及对象permission 字符串写入数据库，用来进行uri授权。  注意里面：需要扫描的controller都要使用@RequestMapping注解， method使用：PostMapping   、
GetMapping   、PutMapping 、DeleteMapping ，不能使用RequestMapping注解， 否则ApiUtils扫描会出错；保持一个method只有一个uri路径；扫描到结果如图：

![image](https://user-images.githubusercontent.com/83743182/123414521-9097e480-d5e6-11eb-9427-5b3aa95ffd1e.png)   
14、MyUserDetailService使用了HttpServletRequest的注入，注意这里是代理模式，每个请求过来，注入的都是该请求对应的request；利用历史请求DefaultSavedRequest获取企业code，实现sas模式。   
![image](https://user-images.githubusercontent.com/83743182/123797363-af64e680-d918-11eb-95a6-dbb9a54e5eed.png)
15、auserve拦截异常的坑，RestControllerAdvice里面如果捕获了InsufficientAuthenticationException，请不要返回错误，继续抛出异常，交由oauth处理，否则请求授权码模式到不了登录页面  
![image](https://user-images.githubusercontent.com/83743182/125550884-38086b97-94e6-40b3-ad2f-9d1338fe64c7.png)
16、json Long型转化，记得添加@JSONField(serializeUsing = LongToStringSerializer.class)； 否则js端会值会异常  ，mybatis的 worker-id: datacenter-id: 配置注意不要重复，避免分布式id产生重复  
![image](https://user-images.githubusercontent.com/83743182/125551509-a6b446f1-d6ac-400f-b641-692a26228622.png)
17、favicon.ico 记得增加如下配置  
![image](https://user-images.githubusercontent.com/83743182/125553851-8da53c9d-e8e7-49a6-bfe4-9ed84d6c2adc.png)
18、启动tokeninfo认证时，swagger的请求需要排除  
![image](https://user-images.githubusercontent.com/83743182/125553924-55000627-7600-4233-8ca9-5ce5bf0b6ded.png)
19、关于超时  
  尽量不要使用synchronized，防止大并发是阻塞线程；使用分布式锁时注意：timeout的设置，大于执行块可能需要的最大时间，否则锁失效造成异常  
20、自定义统用分页查询  ，继承SuperQueryController后，可根据情况在查询前调用adaptiveQueryColumn来调整前端和后端的数据库字段差异  
![image](https://user-images.githubusercontent.com/83743182/125904540-9d9ecf25-3cbb-4d9e-87d5-40a2abd6a17e.png)
注意get 请求是一定要把参数encode下：query: { "pageNum": 2,"pageSize": 2,queryData:[{column:"name",value:"xx",type:"like"  },{column:"note",value:"nnn",type:"ne"}],orderby:[{key:"id",value:"asc"},{key:"name",value:"desc"}]}
21、增加动态rsa非对称加密和定时跟新密钥文件  
22、缓存增加设置不同分类的过期时间  
23、权限过滤，url使用AntPathMatcher进行匹配检索  
24、增加文档断点上传和下载  
![image](https://user-images.githubusercontent.com/83743182/131476532-a5451372-b4d8-4057-bdcf-e5817be2fae3.png)

待续


