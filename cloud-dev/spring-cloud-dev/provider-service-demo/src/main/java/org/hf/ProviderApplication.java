package org.hf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * <p> springboot启动类 </p>
 * //@SpringBootApplication          //springboot启动类注解
 * //@MapperScan("org.hf.**.mapper")         //整合mybatis通用Mapper，扫描该包路径下的mapper接口
 * //@EnableDiscoveryClient          //开启eureka客户端发现功能
 * @author hufei
 * @date 2022/8/21 17:36
*/
@SpringBootApplication
@MapperScan("org.hf.springcloud.provider.service.**.mapper")
@EnableDiscoveryClient
public class ProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }

}
