package org.hf.springcloud.netflix.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @Author:hufei
 * @CreateTime:2020-09-16
 * @Description:springboot启动类
 */
//@SpringBootApplication          //springboot启动类注解
//@EnableDiscoveryClient          //开启eureka客户端发现功能
//@EnableCircuitBreaker           //开启Hystrix熔断器注解
@SpringCloudApplication         //此注解为组合注解，包含以上三个注解
@EnableFeignClients             //开启Feign功能注解
public class ConsumerDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerDemoApplication.class, args);
    }

    @Bean           //将当前方法返回的返回的bean对象交给spring管理，向spring容器中注入一个RestTemplate实例化bean
    @LoadBalanced           //开启Ribbon负载均衡注解，默认是轮询
    public RestTemplate restTemplate() {            //spring对httpClient、okHttp、JDK原生URLConnection进行了封装，RestTemplate的工具类对上述的3种http客户端工具类进行了封装
        return new RestTemplate();
    }
}
