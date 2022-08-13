package org.hf.application.mybatis.multiple.datasource.service.impl;

import org.hf.application.mybatis.multiple.datasource.config.Master;
import org.hf.application.mybatis.multiple.datasource.dao.UserMapper;
import org.hf.application.mybatis.multiple.datasource.pojo.entity.User;
import org.hf.application.mybatis.multiple.datasource.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p> 用户信息处理业务实现类 </p>
 * @author hufei
 * @date 2022/8/13 10:03
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
        //选择性新增，如果实体中字段值为空，则不会出现在insert语句中
        userMapper.insertSelective(user);
        return user;
    }

    @Override
    public User getUser(Long id) {
        return userMapper.getUser(id);
    }

    @Master
    @Override
    public List<User> getUserAll() {
        return userMapper.getUserAll();
    }

    @Override
    public List<User> selectAll() {
        return userMapper.selectAll();
    }

}
