package org.hf.application.mybatis.plus.springboot.service;

import com.github.pagehelper.PageInfo;
import org.hf.application.mybatis.plus.springboot.pojo.entity.UserInfoEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* <p> 用户信息表 服务类 </p>
 *
 * @author hf
 * @since 2022-07-31 00:14
*/
public interface UserInfoService extends IService<UserInfoEntity> {

    UserInfoEntity findById(long id);

    PageInfo<UserInfoEntity> findListUser();

    List<UserInfoEntity> findAll();

}
