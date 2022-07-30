package org.hf.application.mybatis.plus.springboot.mapper;

import org.hf.application.mybatis.plus.springboot.pojo.entity.UserInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
* <p> 用户信息表 Mapper 接口 </p>
 *
 * @author hf
 * @since 2022-07-31 00:14
*/
@Repository
public interface UserInfoMapper extends BaseMapper<UserInfoEntity> {

    UserInfoEntity findById(Long id);

    List<UserInfoEntity> findAll();

}
