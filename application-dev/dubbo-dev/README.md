# dubbo-dev 
dubbo聚合工程

## 1. dubbo-consumer-demo
    springboot服务整合dubbo消费者服务
## 2. dubbo-consumer-web-demo 
    maven-web工程 dubbo服务消费者
    注意: 由于这里依赖的是zk 3.6.2 所有zookeeper要使用3.6.2版本的才行
    由于spring使用的是5.3以上版本, 所以tomcat需要8以上版本
## 3. dubbo-interface-demo 
    dubbo公共接口依赖
## 4. dubbo-provider-demo
    springboot服务整合dubbo提供者服务
## 5. dubbo-provider-web-demo 
    maven-web工程 dubbo服务提供者
    注意: 由于这里依赖的是zk 3.6.2 所有zookeeper要使用3.6.2版本的才行
