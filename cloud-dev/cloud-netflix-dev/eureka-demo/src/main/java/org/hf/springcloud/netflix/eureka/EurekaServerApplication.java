package org.hf.springcloud.netflix.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @Author:hufei
 * @CreateTime:2020-09-16
 * @Description:eureka服务启动类
 */
@EnableEurekaServer         //开启Eureka注解，声明当前应用是一个Eureka服务
@SpringBootApplication          //开启springboot启动类注解
public class EurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
