package org.hf.application.dubbo.provider.boot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.hf.application.dubbo.provider.boot.pojo.entity.UserInfo;
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