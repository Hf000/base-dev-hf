package org.hf.application.mybatis.tk.springboot.mapper;

import org.hf.application.mybatis.tk.springboot.pojo.entity.UserInfo;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * user_info
 * 
 * @author HF
 * @date 2022/08/01 21:38
*/ 
@Repository
public interface UserInfoMapper extends Mapper<UserInfo> {
}