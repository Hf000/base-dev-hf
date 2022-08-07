package org.hf.springcloud.netflix.provider.two.controller;

import org.hf.springcloud.netflix.provider.two.pojo.entity.User;
import org.hf.springcloud.netflix.provider.two.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author:hufei
 * @CreateTime:2020-09-15
 * @Description:用户controller
 */
@RestController         //注解@Controller + @ResponseBody 组合注解，返回json格式字符串
@RequestMapping("/user")            //限定该类的请求路径
public class UserController {

    @Autowired
    private IUserService userServiceImpl;

    @RequestMapping("/{id}")
    public User queryUserById(@PathVariable Long id) {
        /*try {
            Thread.sleep(6000);//测试hystrix请求超时配置
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        System.out.println("这里是8084的调用");
        return userServiceImpl.queryUserById(id);
    }

}
