package org.hf.application.custom.shop.service.impl;

import org.hf.application.custom.shop.dao.UserDao;
import org.hf.application.custom.shop.domain.User;
import org.hf.application.custom.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * <p> 用户业务接口实现 </p>
 *
 * @author hufei
 * @date 2022/7/17 19:56
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User findByUserName(String username) {
        return userDao.findByUserName(username);
    }
}
