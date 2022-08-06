package org.hf.springboot.service.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.hf.springboot.service.pojo.entity.User;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hf
 * @since 2021-11-02 17:18
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    User getUserInfo(Long userId);

}
