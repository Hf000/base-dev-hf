# spring-cloud-dev
spring cloud的分布式解决方案

##1. SpringCloud项目搭建
    1> 启动netflix-eureka-server-demo服务
    2> 启动config-server-demo服务
    3> 启动gateway-demo服务
    4> 启动提供者服务provider-service-demo或者provider-service-demo-two
    5> 启动消费者服务consumer-service-demo
    6> 一个服务通过配置不同端口启动多个实例, 在IDEA的Run/Debug Configurations中复制多个副本设置对应副本的Environment的VM options参数 -Dport=端口号, 
        对应服务工程的yaml文件配置server.port=${port:默认端口号},如果取不到配置端口则取这里的默认端口
##2. netflix-eureka-server-demo工程 
    1> EUreka服务注册中心, 本地查看注册了哪些服务http://127.0.0.1:10086/eureka/apps或者http://127.0.0.1:10086
##3. gateway-demo工程 
    1> Gateway：网关服务，gateway的filter过滤器的执行生命周期与springMVC的拦截器类似，不同的是springMVC的拦截器有三个方法：preHandle（预处理），
        postHandle（后处理，在视图渲染之前调用），afterCompletion（完成时处理，视图渲染完毕后调用，但是preHandle必须返回true才会调用）；而gateway的
        filter只有两个，“pre”和“post”分别会在请求被执行前调用和被执行后调用； 默认集成了Ribbon(ribbon已经被spring-cloud-loadbalancer替换)和Hystrix
    2> 服务请求可以通过访问网关服务,然后路由到对应服务工程, 例如http://127.0.0.1:10010/user/1 会路由到 http://127.0.0.1:8080/user/1
##4. config-server-demo工程
    Config：配置中心，将经常发生改变的配置文件放到git仓库中动态加载，配置中心中yml文件命名规则：{application}-{profile}.yml或.properties
    一般经常修改的配置项放在配置中心，固定不变的的就放在bootstrap.yml配置文件，bootstrap.yml加载优先于application.yml  
##5. cloud-api工程
    feign接口jar
##6. consumer-service-demo工程
    消费者服务集成了OpenFeign和Hystrix
    1.Feign,使服务间的调用更加容易（基于http协议）,实际就是封装了restTemplate，集成了Ribbon(ribbon已经被spring-cloud-loadbalancer替换)和Hystrix熔断配置项，在Feign中可以直接配置相关配置项
    2.Hystrix,服务熔断器：线程隔离，服务降级（如果开启了，那么采用feign进行服务间通信时，是需要重新设置请求头参数的，因为此时所有的请求头参数都会置为空，
        如果需要请求参数，需要将线程池隔离改成信号量隔离）
    3.Ribbon客户端负载均衡器（只是用于负载均衡，不能进行通信，单独使用时实际是基于restTemplate进行服务调用（http协议）），默认算法：轮询
        由于这里使用的spring cloud 2021.0.3版本, ribbon已经被移除,使用spring-cloud-loadbalancer替代, spring-cloud-loadbalancer和
        spring-cloud-starter-netflix-ribbon依赖会冲突,所以建议排除ribbon相关依赖
    4.spring-cloud-loadbalancer负载均衡,默认采用轮询方式,配置成随机参考org.hf.springcloud.consumer.service.config.LoadbalancerConfig;
        依赖spring-retry,集成失败重试功能,具体配置参考properties.yml文件
##7. provider-service-demo、provider-service-demo-two工程
    提供这服务集成了tk-mapper和config配置中心
 