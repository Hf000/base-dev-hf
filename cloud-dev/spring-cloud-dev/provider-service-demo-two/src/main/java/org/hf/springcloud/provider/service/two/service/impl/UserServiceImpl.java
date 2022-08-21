package org.hf.springcloud.provider.service.two.service.impl;

import org.hf.springcloud.provider.service.two.mapper.UserMapper;
import org.hf.springcloud.provider.service.two.pojo.entity.User;
import org.hf.springcloud.provider.service.two.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p> 用户service接口实现类 </p>
 * @author hufei
 * @date 2022/8/21 17:48
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
