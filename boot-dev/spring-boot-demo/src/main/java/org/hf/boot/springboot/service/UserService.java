package org.hf.boot.springboot.service;

import org.hf.boot.springboot.pojo.dto.UserInfoReq;
import org.hf.boot.springboot.pojo.entity.User;
import org.hf.boot.springboot.pojo.entity.UserInfo;

import java.util.Date;
import java.util.List;

/**
 * @Author:hufei
 * @CreateTime:2020-09-09
 * @Description:用户信息处理业务接口
 */
public interface UserService {

    User findUserInfo(Long id);

    User saveUserInfo(String userName, String password, String name, Integer age, Integer sex, Date birthday,
                      String note, Date created, Date updated);

    User getUser(Long id);

    List<User> getUserAll();

    List<User> selectAll();

    List<UserInfo> findUserInfoNew();

    void addUserInfo(UserInfoReq req);

    void addUserInfoAysnc(UserInfoReq req);

    void testAysnc();

    void saveDynamicSourceData(String userName);
}
