package org.hf.application.mybatis.tk.springboot.mapper;

import org.hf.application.mybatis.tk.springboot.pojo.entity.User;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/7/31 19:28
*/
@Repository
public interface UserMapper extends Mapper<User> {
}