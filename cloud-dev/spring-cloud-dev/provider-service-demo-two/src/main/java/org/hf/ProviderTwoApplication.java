package org.hf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * <p> service服务2启动类 </p>
 * //@SpringBootApplication //springBoot启动类注解
 * //@EnableDiscoveryClient //开启Eureka客户端服务注解
 * //@MapperScan("org.hf.**.mapper")    //整合mybatis通用Mapper，扫描该包路径下的mapper接口
 * @author hufei
 * @date 2022/8/21 17:46
*/
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("org.hf.**.mapper")
public class ProviderTwoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProviderTwoApplication.class,args);
    }
}
