# spring-boot-demo 简单使用 (springboot没有父子容器的概念了)
    
1. SpringBoot项目搭建，注意：启动类位置应该高于扫描包中类的位置级别     
2. 整合SpringMVC拦截器    
3. 整合事务和连接池（hikari（默认）、druid）    
4. 整合mybatis、通用Mapper    
5. 整合Junit   
6. 整合redis     
8. 整合多数据源读写分离操作 
9. (已删除)整合elastic-job 注意guava和curator-client的版本兼容问题, 这里guava的版本号是18.0, curator-client的版本号是2.10.0
10. 整合swagger3.0.0, 注意SpringBoot版本是2.6.0以上的需要使用swagger3.0.0版本, swagger本地文档地址: http://localhost:8088/springboot/swagger-ui/index.html
11. org.hf.boot.springboot.annotations包下新增CustomAvoidRepeat防重复提交, CustomPrefixRedisLock自定义锁前缀及名称(支持SpEL)redis锁, CustomRedisLock自定义redis锁(支持锁续期)
