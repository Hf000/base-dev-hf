package org.hf.application.javabase.spring.aop.service;

/**
 * <p> 接口实现类 </p>
 * @author hufei
 * @date 2022/9/3 18:21
*/
public class UserServiceImpl implements UserService{

    @Override
    public void add(){
        System.out.println("增加用户！");
    }
}
