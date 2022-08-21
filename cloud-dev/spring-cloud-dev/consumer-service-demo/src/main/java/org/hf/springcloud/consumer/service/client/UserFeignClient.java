package org.hf.springcloud.consumer.service.client;

import org.hf.springcloud.consumer.service.client.fallback.UserFeignClientFallback;
import org.hf.springcloud.consumer.service.config.FeignConfig;
import org.hf.springcloud.consumer.service.pojo.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * <p> Feign客户端接口 </p>
 * 声明当前类是一个Feign的客户端，1.指定服务名为service-demo, 2.指定该类服务降级的实现类, 3.指定Feign的配置信息， 在Controller中能使用的注解在这里一般都能使用
 * @author hufei
 * @date 2022/8/21 17:10
*/
@FeignClient(value = "service-demo", fallback = UserFeignClientFallback.class, configuration = FeignConfig.class)
public interface UserFeignClient {

    /**
     * 最终拼接出来的地址：http://service-demo/user/id变量
     * @param id 入参
     * @return User
     */
    @GetMapping("/user/{id}")
    User queryById(@PathVariable Long id);
}
