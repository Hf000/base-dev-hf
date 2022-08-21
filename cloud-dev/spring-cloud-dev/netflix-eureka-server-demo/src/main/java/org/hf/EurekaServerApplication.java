package org.hf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * <p> eureka服务启动类 </p>
 * //@EnableEurekaServer    //开启Eureka注解，声明当前应用是一个Eureka服务
 * //@SpringBootApplication //开启springboot启动类注解
 * @author hufei
 * @date 2022/8/21 17:31
*/
@EnableEurekaServer
@SpringBootApplication
public class EurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
