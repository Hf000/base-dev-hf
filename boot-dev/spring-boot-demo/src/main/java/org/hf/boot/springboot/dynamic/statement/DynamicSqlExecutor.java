package org.hf.boot.springboot.dynamic.statement;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 动态sql执行器
 */
@Component
public class DynamicSqlExecutor {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    public List<?> selectList(String statement, Object parameter){
        try (SqlSession sqlSession = sqlSessionFactory.openSession()){
            return sqlSession.selectList(statement,parameter);
        }
    }

    public Object selectOne(String statement,Object parameter){
        try (SqlSession sqlSession = sqlSessionFactory.openSession()){
            return sqlSession.selectOne(statement,parameter);
        }
    }

    public PageInfo<?> selectPage(String statement, Object parameter, Integer pageNum, Integer pageSize){
        try (SqlSession sqlSession = sqlSessionFactory.openSession()){
            PageHelper.startPage(pageNum, pageSize);
            List<?> resultList = sqlSession.selectList(statement, parameter);
            return new PageInfo<>(resultList);
        }
    }
}