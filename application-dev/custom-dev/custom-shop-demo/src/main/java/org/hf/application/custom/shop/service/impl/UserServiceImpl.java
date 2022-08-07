package org.hf.application.custom.shop.service.impl;

import org.hf.application.custom.shop.domain.User;
import org.hf.application.custom.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/7/17 19:56
*/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /***
     * 登录
     * @param username
     * @return
     */
    @Override
    public User findByUserName(String username) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM `user` WHERE username=?",new BeanPropertyRowMapper<User>(User.class),username);
        } catch (Exception e) {
        }
        return null;
    }
}
