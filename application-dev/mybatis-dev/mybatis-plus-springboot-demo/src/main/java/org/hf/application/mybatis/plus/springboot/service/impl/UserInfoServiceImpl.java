package org.hf.application.mybatis.plus.springboot.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.hf.application.mybatis.plus.springboot.pojo.entity.UserInfoEntity;
import org.hf.application.mybatis.plus.springboot.mapper.UserInfoMapper;
import org.hf.application.mybatis.plus.springboot.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p> 用户信息表 服务实现类 </p>
 *
 * @author hf
 * @since 2022-07-31 00:14
*/
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfoEntity> implements UserInfoService {

    @Autowired
    private UserInfoMapper userinfoMapper;

    @Override
    public UserInfoEntity findById(long id) {
        return userinfoMapper.findById(id);
    }

    @Override
    public PageInfo<UserInfoEntity> findListUser() {
        return PageHelper.startPage(1, 3).doSelectPageInfo(() -> userinfoMapper.findAll());
    }

    @Override
    public List<UserInfoEntity> findAll() {
        return userinfoMapper.findAll();
    }

}
