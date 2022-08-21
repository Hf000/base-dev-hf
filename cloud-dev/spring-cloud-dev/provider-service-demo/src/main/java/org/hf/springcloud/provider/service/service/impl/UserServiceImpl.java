package org.hf.springcloud.provider.service.service.impl;

import org.hf.springcloud.provider.service.mapper.UserMapper;
import org.hf.springcloud.provider.service.pojo.entity.User;
import org.hf.springcloud.provider.service.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p> 用户service接口实现类 </p>
 * @author hufei
 * @date 2022/8/21 17:41
*/
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User queryUserById(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }

}
