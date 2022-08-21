package org.hf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * <p> Gateway启动类 </p>
 * //@SpringBootApplication  //springboot启动类注解
 * //@EnableDiscoveryClient  //开启eureka客户端发现功能
 * @author hufei
 * @date 2022/8/21 17:25
*/
@SpringBootApplication
@EnableDiscoveryClient
public class GatewayDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayDemoApplication.class, args);
    }
}
