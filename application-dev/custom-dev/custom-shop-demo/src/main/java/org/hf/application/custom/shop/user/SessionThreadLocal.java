package org.hf.application.custom.shop.user;

import org.springframework.stereotype.Component;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/7/17 20:04
*/
@Component
public class SessionThreadLocal {

    //1.创建一个ThreadLocal实现存储线程下共享的对象
    private static ThreadLocal<Session> sessions = new ThreadLocal<Session>();

    //2.添加共享对象
    public void add(Session session){
        sessions.set(session);
    }

    //3.获取共享对象
    public Session get(){
        return sessions.get();
    }

    //4.移除共享对象
    public void remove(){
        sessions.remove();
    }
}
