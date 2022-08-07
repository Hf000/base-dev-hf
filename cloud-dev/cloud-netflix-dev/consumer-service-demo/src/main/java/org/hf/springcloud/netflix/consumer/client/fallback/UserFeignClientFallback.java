package org.hf.springcloud.netflix.consumer.client.fallback;

import org.hf.springcloud.netflix.consumer.client.UserFeignClient;
import org.hf.springcloud.netflix.consumer.pojo.entity.User;
import org.springframework.stereotype.Component;

/**
*@Author:hufei
*@CreateTime:2020-10-23
*@Description:Feign服务降级
*/
@Component
public class UserFeignClientFallback implements UserFeignClient {

    @Override
    public User QueryById(Long id) {
        User user = new User();
        user.setId(id);
        user.setName("用户异常");
        return user;
    }
}
