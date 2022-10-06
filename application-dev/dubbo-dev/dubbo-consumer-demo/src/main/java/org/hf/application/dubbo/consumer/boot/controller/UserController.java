package org.hf.application.dubbo.consumer.boot.controller;

import org.apache.dubbo.common.constants.LoadbalanceRules;
import org.apache.dubbo.config.annotation.DubboReference;
import org.hf.application.dubbo.interfac.pojo.UserVO;
import org.hf.application.dubbo.interfac.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>  </p >
 * @author hufei
 * @date 2022-09-27
 **/
@RestController
public class UserController {

    /**
     * 1.接口类型, 2.是否启动检查, 3.重试次数, 4.超时时间, 5.版本号, 6.负载均衡
     */
    @DubboReference(interfaceClass = UserService.class, check = false, retries = 3, timeout = 6000, version = "3.1.0", loadbalance = LoadbalanceRules.RANDOM)
    private UserService userService;

    @GetMapping(value = "/findUser")
    public List<UserVO> findUserList() {
        return userService.findUserList();
    }

}