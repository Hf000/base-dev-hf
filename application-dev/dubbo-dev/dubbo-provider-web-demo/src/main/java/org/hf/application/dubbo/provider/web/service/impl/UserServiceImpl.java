package org.hf.application.dubbo.provider.web.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import org.hf.application.dubbo.provider.web.mapper.TestMapper;
import org.hf.application.dubbo.provider.web.mapper.UserMapper;
import org.hf.application.dubbo.interfac.pojo.UserVO;
import org.hf.application.dubbo.interfac.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: hufei
 * @date: 2020/12/12 17:11
 * @version: 1.0.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TestMapper testMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public String getNow() {
        System.out.println("service方法被调用了！");
        String now = testMapper.getNow();
        return now;
    }

    @Override
    public List<UserVO> findUserList() {
        PageInfo<UserVO> objectPageInfo = PageMethod.startPage(1, 10).doSelectPageInfo(() -> userMapper.findUserList());
        return objectPageInfo.getList();
    }
}
