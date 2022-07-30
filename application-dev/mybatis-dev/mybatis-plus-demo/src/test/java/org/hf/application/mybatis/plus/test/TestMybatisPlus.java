package org.hf.application.mybatis.plus.test;

import com.baomidou.mybatisplus.core.MybatisSqlSessionFactoryBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.hf.application.mybatis.plus.mapper.UserMapper;
import org.hf.application.mybatis.plus.pojo.entity.User;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

public class TestMybatisPlus {

    @Test
    public void testFindAll() throws Exception{

        String config = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(config);
        //MybatisSqlSessionFactoryBuilder这个对象不是mybatis原生的对象，而是mybatis-plus定义的对象
        SqlSessionFactory sqlSessionFactory = new MybatisSqlSessionFactoryBuilder().build(inputStream);

        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        //测试查询
//        List<User> users = userMapper.findAll();
        List<User> users = userMapper.selectList(null);
        for (User user : users) {
            System.out.println(user);
        }

    }
}
