package org.hf;

import org.hf.springcloud.consumer.service.config.LoadbalancerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClientConfiguration;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * <p> 消费服务启动类 </p>
 * //@SpringBootApplication          //springboot启动类注解
 * //@EnableDiscoveryClient          //开启eureka客户端发现功能
 * //@EnableCircuitBreaker           //开启Hystrix熔断器注解,已过时,可以使用@EnableHystrix代替
 * //@EnableHystrix                  // 开启Hystrix熔断器
 * //@SpringCloudApplication         //此注解为组合注解，包含以上三个注解 ,高版本中此注解已过时,去掉了开启Hystrix熔断器注解
 * //@EnableFeignClients             //开启Feign功能注解
 * //@LoadBalancerClients(value = {@LoadBalancerClient(name = "service-demo", configuration = LoadbalancerConfig.class)}, defaultConfiguration = LoadBalancerClientConfiguration.class) spring-cloud-loadbalancer负载均衡策略配置
 * @author hufei
 * @date 2022/8/21 16:23
*/
@LoadBalancerClients(value = {@LoadBalancerClient(name = "service-demo", configuration = LoadbalancerConfig.class)}, defaultConfiguration = LoadBalancerClientConfiguration.class)
@EnableHystrix
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"org.hf.springcloud.*.*.client"})
public class ConsumerDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerDemoApplication.class, args);
    }

    /**
     * //@Bean  //将当前方法返回的返回的bean对象交给spring管理，向spring容器中注入一个RestTemplate实例化bean
     * //@LoadBalanced  //开启Ribbon(这里已经被spring-cloud-loadbalancer替代)负载均衡注解，默认是轮询
     * @return RestTemplate
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {            //spring对httpClient、okHttp、JDK原生URLConnection进行了封装，RestTemplate的工具类对上述的3种http客户端工具类进行了封装
        return new RestTemplate();
    }
}
