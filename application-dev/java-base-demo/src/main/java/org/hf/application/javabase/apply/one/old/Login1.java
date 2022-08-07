package org.hf.application.javabase.apply.one.old;

import java.sql.Connection;
import java.sql.ResultSet;


public class Login1 {

    //①登录，用户请求该方法
    public String login(String username,String password){
        Boolean bo = validate(username,password);   //参数校验
        Object user = findUser(username, password); //查找用户
        if(user==null){
            return "登录失败";
        }
        return "SUCCESS";
    }

    //②参数校验
    public Boolean validate(String username,String password){
        return true;
    }

    //③查找用户信息
    public Object findUser(String username,String password){
        return null;
    }

    //④链接数据库
    public Connection getConn(){
        return null;
    }

    //⑤操作数据库
    public ResultSet query(){
        return null;
    }
}
