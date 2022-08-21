package org.hf.springcloud.consumer.service.client.fallback;

import org.hf.springcloud.consumer.service.client.UserFeignClient;
import org.hf.springcloud.consumer.service.pojo.entity.User;
import org.springframework.stereotype.Component;

/**
 * <p> Feign服务降级 </p>
 * @author hufei
 * @date 2022/8/21 17:11
*/
@Component
public class UserFeignClientFallback implements UserFeignClient {

    @Override
    public User queryById(Long id) {
        User user = new User();
        user.setId(id);
        user.setName("用户异常");
        return user;
    }
}
