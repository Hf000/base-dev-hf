package org.hf.application.mybatis.plus.springboot.test;

import org.hf.application.mybatis.plus.springboot.mapper.UserInfoMapper;
import org.hf.application.mybatis.plus.springboot.pojo.entity.UserInfoEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestMybatisSpringBoot {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Test
    public void testSelectList(){
        List<UserInfoEntity> users = this.userInfoMapper.selectList(null);
        for (UserInfoEntity user : users) {
            System.out.println(user);
        }
    }

}
