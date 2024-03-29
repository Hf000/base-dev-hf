package org.hf.application.custom.shop.dao.impl;

import org.hf.application.custom.shop.dao.UserDao;
import org.hf.application.custom.shop.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * <p> 用户数据接口实现 </p>
 *
 * @author hufei
 * @date 2022/7/17 19:58
 */
@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public User findByUserName(String username) {
        return jdbcTemplate.queryForObject("select * from user where username=?", new BeanPropertyRowMapper<User>(User.class), username);
    }

    @Override
    public void modifyGold(String username, int remaining) {
        jdbcTemplate.update("update user set gold=? where username=?", remaining, username);
    }
}
