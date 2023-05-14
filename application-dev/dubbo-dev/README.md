# dubbo-dev 
dubbo聚合工程

## 1. dubbo-consumer-demo
    springboot服务整合dubbo消费者服务
## 2. dubbo-consumer-web-demo 
    maven-web工程 dubbo服务消费者
    注意: 由于这里依赖的是zk 3.6.2 所有zookeeper要使用3.6.2版本的才行(其他版本的排除掉)
    由于spring使用的是5.3以上版本, 所以tomcat需要8以上版本
## 3. dubbo-interface-demo 
    dubbo公共接口依赖
## 4. dubbo-provider-demo
    springboot服务整合dubbo提供者服务
## 5. dubbo-provider-web-demo 
    maven-web工程 dubbo服务提供者
    注意: 由于这里依赖的是zk 3.6.2 所有zookeeper要使用3.6.2版本的才行(其他版本的排除掉)
## 6. 注意点
    1 集成到Spring-service项目环境，dubbo的@Service注解不能实例化对象；
        1> 在spring环境中集成dubbo的@Service注解，需要在配置文件中添加dubbo的注解扫描配置
        2> <dubbo:annotation package="com.dubbo.service.impl"/>
    2 集成到Spring-Web项目环境，dubbo的@Reference注入对象为null的问题。
        1> 在springMVC中集成dubbo的@Reference注解，需要在MVC的配置文件中的注解扫描之前先扫描dubbo的@Reference注解，所以在配置注解扫描时的顺序是
        2> 先配置dubbo的注解扫描：<dubbo:annotation package="com.dubbo.controller" />
        3> 再配置springMVC的注解扫描：<context:component-scan base-package="com.dubbo.controller" />
        4> 原因：因为如果先扫描了springMVC的注解，那么此时控制器就已经将controller的实例化好了，dubbo的实例对象就无法注入了，所以需要先实例化好dubbo的注入对象。
        5> 注意: 如果是需要注入dubbo的服务对象，那么在spring容器实例化Bean之前，需要先实例化dubbo服务对象才能在Spring的Bean中注入。

