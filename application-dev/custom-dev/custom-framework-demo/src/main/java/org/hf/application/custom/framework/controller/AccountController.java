package org.hf.application.custom.framework.controller;

import org.hf.application.custom.framework.pojo.entity.User;
import org.hf.application.custom.framework.framework.annotation.RequestMapping;
import org.hf.application.custom.framework.service.AccountService;

/**
 * <p> controller </p>
 *
 * @author hufei
 * @date 2022/7/17 19:22
 */
public class AccountController {

    private AccountService accountService;

    /**
     * 查询一条记录示例
     */
    @RequestMapping(value = "/account/one")
    public String one() {
        System.out.println("执行了one!");
        String result = accountService.one();
        return "/WEB-INF/pages/one.jsp";
    }

    /**
     * 查询用户信息
     */
    @RequestMapping(value = "/account/info")
    public User info() {
        User user = new User();
        user.setName("王五");
        user.setAddress("深圳");
        return user;
    }
}
