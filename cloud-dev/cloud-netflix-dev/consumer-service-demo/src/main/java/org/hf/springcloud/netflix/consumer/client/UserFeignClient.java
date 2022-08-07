package org.hf.springcloud.netflix.consumer.client;

import org.hf.springcloud.netflix.consumer.client.fallback.UserFeignClientFallback;
import org.hf.springcloud.netflix.consumer.config.FeignConfig;
import org.hf.springcloud.netflix.consumer.pojo.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author:hufei
 * @CreateTime:2020-10-23
 * @Description:Feign客户端接口
 */
//声明当前类是一个Feign的客户端，1.指定服务名为service-demo, 2.指定该类服务降级的实现类, 3.指定Feign的配置信息， 在Controller中能使用的注解在这里一般都能使用
@FeignClient(value = "service-demo", fallback = UserFeignClientFallback.class, configuration = FeignConfig.class)
public interface UserFeignClient {

    @GetMapping("/user/{id}")       //最终拼接出来的地址：http://service-demo/user/id变量
    User QueryById(@PathVariable Long id);
}
