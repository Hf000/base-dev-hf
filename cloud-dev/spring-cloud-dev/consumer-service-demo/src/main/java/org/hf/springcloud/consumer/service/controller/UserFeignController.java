package org.hf.springcloud.consumer.service.controller;

import org.hf.springcloud.api.provider.client.UserFeignClient;
import org.hf.springcloud.api.provider.pojo.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> Feign的处理器 </p>
 * @author hufei
 * @date 2022/8/21 17:18
*/
@RestController
@RequestMapping("/user")
public class UserFeignController {

    /**
     * 注入Feign客户端
     */
    @Autowired
    private UserFeignClient userFeignClient;

    @GetMapping("/{id}")
    public UserDto queryById(@PathVariable Long id){
        return userFeignClient.queryById(id);
    }

}
