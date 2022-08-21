# spring-cloud-dev
spring cloud的分布式解决方案

##1. SpringCloud项目搭建
##2. EUreka服务注册中心
##3. Ribbon客户端负载均衡器（只是用于负载均衡，不能进行通信，单独使用时实际是基于restTemplate进行服务调用（http协议）），默认算法：轮询
##4. Hystrix,服务熔断器：线程隔离，服务降级（如果开启了，那么采用feign进行服务间通信时，是需要重新设置请求头参数的，因为此时所有的请求头参数都会置为空，
    如果需要请求参数，需要将线程池隔离改成信号量隔离）
##5. Feign,使服务间的调用更加容易（基于http协议）,实际就是封装了restTemplate，集成了Ribbon和Hystrix熔断配置项，在Feign中可以直接配置相关配置项
##6. Gateway：网关服务，gateway的filter过滤器的执行生命周期与springMVC的拦截器类似，不同的是springMVC的拦截器有三个方法：preHandle（预处理），
    postHandle（后处理，在视图渲染之前调用），afterCompletion（完成时处理，视图渲染完毕后调用，但是preHandle必须返回true才会调用）；而gateway的
    filter只有两个，“pre”和“post”分别会在请求被执行前调用和被执行后调用； 默认集成了Ribbon和Hystrix
##7. Config：配置中心，将经常发生改变的配置文件放到git仓库中动态加载，配置中心中yml文件命名规则：{application}-{profile}.yml或.properties
    一般经常修改的配置项放在配置中心，固定不变的的就放在bootstrap.yml配置文件，bootstrap.yml加载优先于application.yml  
