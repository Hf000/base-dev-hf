package org.hf.application.javabase.apply.one.news;

import org.springframework.beans.factory.annotation.Autowired;


public class LoginController {

    @Autowired
    private LoginService loginService;

    //①登录，用户请求该方法
    public String login(String username,String password){
        Boolean bo = validate(username,password);   //参数校验
        Object user = loginService.findUser(username, password); //查找用户
        if(user==null){
            return "登录失败";
        }
        return "SUCCESS";
    }


    //②参数校验
    public Boolean validate(String username,String password){
        return true;
    }
}
