package org.hf.application.custom.shop.dao;

import org.hf.application.custom.shop.domain.User;

/**
 * <p> 用户信息查询接口 </p>
 *
 * @author hufei
 * @date 2022/7/17 19:56
 */
public interface UserDao {

    /**
     * 根据username查询用户
     * @param username 用户名称
     * @return 用户信息
     */
    User findByUserName(String username);

    /**
     * 修改用户金币
     * @param username 用户名称
     * @param remaining 金币
     */
    void modifyGold(String username, int remaining);
}
