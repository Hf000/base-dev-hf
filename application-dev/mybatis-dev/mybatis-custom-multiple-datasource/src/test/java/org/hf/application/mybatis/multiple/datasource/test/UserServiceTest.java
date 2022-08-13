package org.hf.application.mybatis.multiple.datasource.test;

import org.hf.application.mybatis.multiple.datasource.pojo.entity.User;
import org.hf.application.mybatis.multiple.datasource.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

/**
 * <p> 测试类 </p>
 * @author hufei
 * @date 2022/8/13 10:05
*/
@SpringBootTest                     //开启springboot项目测试注解
public class UserServiceTest {

    @Autowired
    private UserService userServiceImpl;

    @Test
    public void findUserInfo() {
        User user = userServiceImpl.findUserInfo(8l);
        System.out.println("user信息 = "+user);
    }

    @Test
    public void saveUserInfo() {
        User user = userServiceImpl.saveUserInfo("test12","123456", "test7", 10, 1, new Date(),
                null, null, null);
        System.out.println("新增user信息 = "+user);
    }

    @Test
    public void getUser() {
        User user = userServiceImpl.getUser(8l);
        System.out.println(user);
    }

    @Test
    public void getUserAll() {
        List<User> users = userServiceImpl.getUserAll();
        users.forEach(System.out::println);
    }

    @Test
    public void selectUserAll() {
        List<User> users = userServiceImpl.selectAll();
        users.forEach(System.out::println);
    }

}