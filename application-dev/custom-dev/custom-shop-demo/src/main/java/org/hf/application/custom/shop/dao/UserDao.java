package org.hf.application.custom.shop.dao;

import org.hf.application.custom.shop.domain.User;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/7/17 19:56
*/
public interface UserDao {
    /***
     * 根据username查询用户
     * @param username
     * @return
     */
    User findByUserName(String username);

    /***
     * 修改用户金币
     * @param username
     * @param remaining
     */
    void modifyGold(String username, int remaining);
}
