package org.hf.springcloud.netflix.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
*@Author:hufei
*@CreateTime:2020-10-23
*@Description:Gateway启动类
*/
@SpringBootApplication  //springboot启动类注解
@EnableDiscoveryClient  //开启eureka客户端发现功能
public class GatewayDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayDemoApplication.class, args);
    }
}
