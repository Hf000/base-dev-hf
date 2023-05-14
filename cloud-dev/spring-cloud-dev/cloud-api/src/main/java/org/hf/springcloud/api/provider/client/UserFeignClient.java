package org.hf.springcloud.api.provider.client;

import org.hf.springcloud.api.provider.client.fallback.UserFeignClientFallback;
import org.hf.springcloud.api.provider.config.FeignConfig;
import org.hf.springcloud.api.provider.pojo.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * <p> Feign客户端接口 </p>
 * 声明当前类是一个Feign的客户端，声明当前类是一个Feign的客户端，
 * 1.指定服务名为service-demo,
 * 2.指定该类服务降级的实现类,
 * 3.指定Feign的配置信息,
 * 4.容器中的唯一标识,
 * 5.请求父路径，
 *
 * @author hufei
 * @date 2022/8/21 17:10
 */
@FeignClient(value = "service-demo", fallback = UserFeignClientFallback.class, configuration = FeignConfig.class, contextId = "userFeignClient", path = "/user")
public interface UserFeignClient {

    /**
     * 最终拼接出来的地址：http://service-demo/user/id变量
     * @param id 入参
     * @return User
     */
    @GetMapping("/{id}")
    UserDto queryById(@PathVariable Long id);
}
