package org.hf.application.dubbo.provider.web.mapper;

import org.hf.application.dubbo.interfac.pojo.UserVO;

import java.util.List;

public interface UserMapper {
    List<UserVO> findUserList();
}