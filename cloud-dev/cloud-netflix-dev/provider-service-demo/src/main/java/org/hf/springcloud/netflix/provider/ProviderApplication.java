package org.hf.springcloud.netflix.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Author:hufei
 * @CreateTime:2020-09-14
 * @Description:springboot启动类
 */
@SpringBootApplication          //springboot启动类注解
@MapperScan("org.hf.**.mapper")         //整合mybatis通用Mapper，扫描该包路径下的mapper接口
@EnableDiscoveryClient          //开启eureka客户端发现功能
public class ProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }

}
