package org.hf.application.custom.shop.service;

import org.hf.application.custom.shop.domain.User;

/**
 * <p> 用户业务接口 </p>
 * @author hufei
 * @date 2022/7/17 20:03
*/
public interface UserService {

    /**
     * 根据用户名查询登录
     * @param username 用户名称
     * @return 用户信息
     */
    User findByUserName(String username);
}
