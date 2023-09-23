package org.hf.boot.springboot.service.impl;

import org.hf.boot.springboot.annotations.CustomTransactional;
import org.hf.boot.springboot.constants.RetryTypeEnum;
import org.hf.boot.springboot.dao.UserInfoMapper;
import org.hf.boot.springboot.dao.UserMapper;
import org.hf.boot.springboot.pojo.dto.UserInfoReq;
import org.hf.boot.springboot.pojo.entity.User;
import org.hf.boot.springboot.pojo.entity.UserInfo;
import org.hf.boot.springboot.retry.CustomRetryException;
import org.hf.boot.springboot.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p> 用户信息处理业务实现类 </p>
 * @author hufei
 * @date 2022/9/25 16:23
*/
@Service
public class UserServiceImpl implements UserService {

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    @Cacheable(cacheNames = "ONE_MIN_CACHE")
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
    @CustomRetryException(serviceCode = "UserServiceImpl", retryType = RetryTypeEnum.METHOD)
    public List<User> getUserAll() {
        int a = 1/0;
        return userMapper.getUserAll();
    }

    @Override
    public List<User> selectAll() {
        return userMapper.selectAll();
    }

    @Override
    public List<UserInfo> findUserInfoNew() {
        return userInfoMapper.selectAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUserInfo(UserInfoReq req) {
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(req, userInfo);
        userInfoMapper.insertSelective(userInfo);
    }

    @Override
    @CustomTransactional(rollbackFor = Exception.class)
    public void addUserInfoAysnc(UserInfoReq req) {
        for (int i = 0; i < 3; i++) {
            asynAddUserInfo(req, i);
        }
    }

    @Async("customTaskExecutor")
    public void asynAddUserInfo(UserInfoReq req, Integer count) {
        if (count.equals(2)) {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException("测试报错");
            }
        }
        req.setUserName(req.getUserName() + count);
        addUserInfo(req);
        System.out.println(Thread.currentThread().getName() + "线程测试" + count);
    }

}
