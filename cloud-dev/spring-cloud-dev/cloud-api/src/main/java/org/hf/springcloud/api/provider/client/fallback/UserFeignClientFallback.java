package org.hf.springcloud.api.provider.client.fallback;

import org.hf.springcloud.api.provider.client.UserFeignClient;
import org.hf.springcloud.api.provider.pojo.dto.UserDto;
import org.springframework.stereotype.Component;

/**
 * <p> Feign服务降级 </p>
 * @author hufei
 * @date 2022/8/21 17:11
*/
@Component
public class UserFeignClientFallback implements UserFeignClient {

    @Override
    public UserDto queryById(Long id) {
        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setName("用户异常");
        return userDto;
    }
}
