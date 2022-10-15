package org.hf.springcloud.provider.service.two.controller;

import lombok.extern.slf4j.Slf4j;
import org.hf.springcloud.api.provider.client.UserFeignClient;
import org.hf.springcloud.api.provider.pojo.dto.UserDto;
import org.hf.springcloud.provider.service.two.pojo.entity.User;
import org.hf.springcloud.provider.service.two.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * <p> 用户controller </p>
 * //@RestController    //注解@Controller + @ResponseBody 组合注解，返回json格式字符串
 * //@RequestMapping("/user")   //限定该类的请求路径
 * @author hufei
 * @date 2022/8/21 17:47
*/
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController implements UserFeignClient {

    @Autowired
    private IUserService userServiceImpl;

    @RequestMapping("/{id}")
    public User queryUserById(@PathVariable Long id) {
        /*try {
            //测试hystrix请求超时配置
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        System.out.println("这里是8084的调用");
        return userServiceImpl.queryUserById(id);
    }

    @Override
    @GetMapping("/{id}")
    public UserDto queryById(@PathVariable Long id) {
        log.info("这里是two走feign接口");
        User user = userServiceImpl.queryUserById(id);
        UserDto dto = new UserDto();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }
}
