package org.hf.application.dubbo.interfac.service;

import org.hf.application.dubbo.interfac.pojo.UserVO;

import java.util.List;

/**
 * <p> dubbo接口 </p>
 * @author hufei
 * @date 2022/8/6 14:29
*/
public interface UserService {

    String getNow();

    List<UserVO> findUserList();
}
