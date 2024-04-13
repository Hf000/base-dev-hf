package org.hf.springboot.service.pojo.bo;

import org.hf.common.publi.exception.BusinessException;
import org.hf.springboot.service.pojo.dto.UserInfoDTO;

/**
 * <p> 存放线程用户上下文，供单次请求简单从上下文获取用户信息。 </p >
 * 注意: treadLocal的使用一定要注意使用完进行数据清理, 避免线程污染
 * @author hufei
 * @date 2022-09-05
 **/
public class UserContext {

    private static final ThreadLocal<Long> THREAD_LOCAL_TIME = new ThreadLocal<>();
    private static final ThreadLocal<UserInfoDTO> THREAD_LOCAL_USER = new ThreadLocal<>();

    public static void addTime(Long transaction) {
        THREAD_LOCAL_TIME.set(transaction);
    }

    public static Long getTime() {
        return THREAD_LOCAL_TIME.get();
    }

    public static void addUser(UserInfoDTO user) {
        THREAD_LOCAL_USER.set(user);
    }

    public static UserInfoDTO getUser() {
        return THREAD_LOCAL_USER.get();
    }

    /**
     * 安全获取用户，获取不到抛出异常
     *
     * @return 用户
     */
    public static UserInfoDTO safeGetUser() throws BusinessException {
        UserInfoDTO userInfoDto = THREAD_LOCAL_USER.get();
        if (userInfoDto == null) {
            throw new BusinessException("请先登录");
        }
        return userInfoDto;
    }

    public static void removeUser() {
        THREAD_LOCAL_TIME.remove();
        THREAD_LOCAL_USER.remove();
    }

    public static boolean isExistsUser() {
        return getUser() != null;
    }

    /**
     * 获取当前用户Um信息
     */
    public static String getUsername() {
        if (isExistsUser()) {
            return getUser().getUsername();
        } else {
            return null;
        }
    }

    /**
     * 获取当前用户ID
     */
    public static Integer getUserId() {
        if (isExistsUser()) {
            return getUser().getUserId();
        } else {
            return null;
        }
    }

    public static Integer safeGetUserId() throws BusinessException {
        Integer userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException("请先登录");
        }
        return userId;
    }

}