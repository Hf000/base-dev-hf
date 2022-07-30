package org.hf.application.mybatis.plus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.hf.application.mybatis.plus.pojo.entity.User;

import java.util.List;

/**
 * mybatis-plus：需要继承BaseMapper这个对象
 * @author HF
 */
public interface UserMapper extends BaseMapper<User> {

    List<User> findAll();
}
