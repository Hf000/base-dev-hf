package org.hf.springcloud.netflix.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @Author:hufei
 * @CreateTime:2020-11-13
 * @Description:配置中心启动类
 */
@SpringBootApplication      //springboot启动类注解
@EnableConfigServer         //开启配置中心服务注解
public class ConfigDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigDemoApplication.class, args);
    }
}
