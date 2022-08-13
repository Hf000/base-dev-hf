package org.hf.boot.springboot.service.impl;

import org.hf.boot.springboot.dao.UserMapper;
import org.hf.boot.springboot.pojo.entity.User;
import org.hf.boot.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author:hufei
 * @CreateTime:2020-09-09
 * @Description:用户信息处理业务实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired(required = false)
    private UserMapper userMapper;

    @Override
    public User findUserInfo(Long id) {
        System.out.println("find success");
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    @Transactional
    public User saveUserInfo(String userName, String password, String name, Integer age, Integer sex, Date birthday,
                             String note, Date created, Date updated) {
        User user = new User();
        user.setName(name);
        user.setAge(age);
        user.setBirthday(birthday);
        user.setUserName(userName);
        user.setPassword(password);
        user.setSex(sex);
        user.setNote(note);
        user.setCreated(created);
        user.setUpdated(updated);
        userMapper.insertSelective(user);      //选择性新增，如果实体中字段值为空，则不会出现在insert语句中
        return user;
    }

    @Override
    public User getUser(Long id) {
        return userMapper.getUser(id);
    }

    @Override
    public List<User> getUserAll() {
        return userMapper.getUserAll();
    }

    @Override
    public List<User> selectAll() {
        return userMapper.selectAll();
    }

}
