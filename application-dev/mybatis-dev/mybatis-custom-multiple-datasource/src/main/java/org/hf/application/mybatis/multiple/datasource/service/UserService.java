package org.hf.application.mybatis.multiple.datasource.service;


import org.hf.application.mybatis.multiple.datasource.pojo.entity.User;

import java.util.Date;
import java.util.List;

/**
 * <p> 用户信息处理业务接口 </p>
 * @author hufei
 * @date 2022/8/13 10:02
*/
public interface UserService {

    User findUserInfo(Long id);

    User saveUserInfo(String userName, String password, String name, Integer age, Integer sex, Date birthday,
                      String note, Date created, Date updated);

    User getUser(Long id);

    List<User> getUserAll();

    List<User> selectAll();

}
