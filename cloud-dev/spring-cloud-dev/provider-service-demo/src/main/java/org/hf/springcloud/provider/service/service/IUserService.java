package org.hf.springcloud.provider.service.service;

import org.hf.springcloud.provider.service.pojo.entity.User;

/**
 * <p> 用户service接口 </p>
 * @author hufei
 * @date 2022/8/21 17:40
*/
public interface IUserService {

    /**
     * 查询用户信息
     * @param id 入参
     * @return User
     */
    User queryUserById(Long id);

}
