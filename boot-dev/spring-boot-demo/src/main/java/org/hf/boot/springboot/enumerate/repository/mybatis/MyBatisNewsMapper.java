package org.hf.boot.springboot.enumerate.repository.mybatis;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

/**
 * mybatis数据层接口
 */
public interface MyBatisNewsMapper {

    @Insert("insert test_enum (status) value (#{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(MyBatisNewsEntity entity);

    @Select("select id, status from test_enum where id = #{id}")
    MyBatisNewsEntity findById(Integer id);
}