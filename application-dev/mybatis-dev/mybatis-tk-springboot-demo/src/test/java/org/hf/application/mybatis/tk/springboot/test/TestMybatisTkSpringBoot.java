package org.hf.application.mybatis.tk.springboot.test;

import org.hf.application.mybatis.tk.springboot.mapper.UserMapper;
import org.hf.application.mybatis.tk.springboot.pojo.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * <p>  </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/2/21 22:08
 */
@SpringBootTest
public class TestMybatisTkSpringBoot {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelectList(){
        List<User> users = this.userMapper.selectAll();
        users.forEach(System.out::println);
    }

}
