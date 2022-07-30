package org.hf.application.mybatis.plus.test;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.hf.application.mybatis.plus.mapper.UserMapper;
import org.hf.application.mybatis.plus.pojo.entity.User;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

public class TestMybatis {

    @Test
    public void testFindAll() throws Exception{

        /*获取session工厂*/
        //配置mybatis配置文件的名称
        String config = "mybatis-config.xml";
        //根据配置文件名称加载配置文件
        InputStream inputStream = Resources.getResourceAsStream(config);
        //创建session工厂，将指定的配置文件设置到工厂中
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        /*获取sqlSession*/
        //从工厂中获取sqlsession
        SqlSession sqlSession = sqlSessionFactory.openSession();
        //从sqlsession中获取对应Mapper
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        //测试查询
        List<User> users = userMapper.findAll();
        for (User user : users) {
            System.out.println(user);
        }

    }
}
