package org.hf.application.dubbo.provider.boot.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.hf.application.dubbo.interfac.pojo.UserVO;
import org.hf.application.dubbo.interfac.service.UserService;
import org.hf.application.dubbo.provider.boot.mapper.UserInfoMapper;
import org.hf.application.dubbo.provider.boot.pojo.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>  </p >
 * @author hufei
 * @date 2022-09-27
 **/
@DubboService(interfaceClass = UserService.class, version = "3.1.0")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public String getNow() {
        System.out.println("service方法被调用了！");
        return "hello";
    }

    @Override
    public List<UserVO> findUserList() {
        UserInfo userInfo = new UserInfo();
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(userInfo);
        List<UserInfo> userInfos = userInfoMapper.selectList(queryWrapper);
        return BeanUtil.copyToList(userInfos, UserVO.class);
    }
}
