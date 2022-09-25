package org.hf.boot.springboot.dao;

import org.hf.boot.springboot.mapper.BaseMapper;
import org.hf.boot.springboot.pojo.entity.UserInfo;
import org.springframework.stereotype.Repository;

/**
 * 用户信息表
 * user_info
 * 
 * @author hufei
 * @date 2022/09/22 11:15
*/ 
@Repository
public interface UserInfoMapper extends BaseMapper<UserInfo> {
}