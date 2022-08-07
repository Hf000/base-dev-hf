package org.hf.springcloud.netflix.provider.two.service;

import org.hf.springcloud.netflix.provider.two.pojo.entity.User;

/**
 * @Author:hufei
 * @CreateTime:2020-09-15
 * @Description:用户service接口
 */
public interface IUserService {

    User queryUserById(Long id);

}
