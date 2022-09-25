# spring-boot-web-dev 
SpringBoot项目web和service分离聚合服务

## 1. spring-boot-web-demo
    1> 获取枚举类枚举项的公共接口: EnumController
    2> 执行定时任务相关操作接口: ElasticJobController
    3> 事件发布方式: EventController
    4> 常量获取接口: ConstantController
## 2. spring-boot-web-service
    1> 集成mybatis-plus, 提供一些已有的数据库操作方法
    2> 集成mapstruct时存在的问题, mapstruct会导致lombok生成代码不生效, 解决:参考此工程的pom.xml的插件配置, 或参考mapstruct官网的解决方案。
## 3. 配置集成
    1> 请求过滤器配置: org.hf.springboot.web.filter.RequestFilter
    2> 请求拦截器配置: org.hf.springboot.web.interceptor.RequestInterceptor
    3> mybatis拦截器配置: org.hf.springboot.service.config.MyBatisPluginsConfig
    4> AOP日志拦截配置: org.hf.springboot.web.aop.CustomLogAspect
