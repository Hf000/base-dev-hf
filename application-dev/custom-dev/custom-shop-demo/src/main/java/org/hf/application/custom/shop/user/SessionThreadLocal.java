package org.hf.application.custom.shop.user;

import org.springframework.stereotype.Component;

/**
 * <p> 请求会话信息共享处理 </p>
 *
 * @author hufei
 * @date 2022/7/17 20:04
 */
@Component
public class SessionThreadLocal {

    /**
     * 1.创建一个ThreadLocal实现存储线程下共享的对象
     */
    private static final ThreadLocal<Session> SESSIONS = new ThreadLocal<>();

    /**
     * 2.添加共享对象
     * @param session 会话信息
     */
    public void add(Session session) {
        SESSIONS.set(session);
    }

    /**
     * 3.获取共享对象
     * @return 会话信息
     */
    public Session get() {
        return SESSIONS.get();
    }

    /**
     * 4.移除共享对象
     */
    public void remove() {
        SESSIONS.remove();
    }
}
