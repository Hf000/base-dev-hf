package org.hf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * <p> 配置中心启动类 </p>
 * //@SpringBootApplication      //springboot启动类注解
 * //@EnableConfigServer         //开启配置中心服务注解
 * @author hufei
 * @date 2022/8/21 16:19
*/
@SpringBootApplication
@EnableConfigServer
public class ConfigDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigDemoApplication.class, args);
    }
}
