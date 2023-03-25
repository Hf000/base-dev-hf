package org.hf.application.custom.shop.user;

/**
 * <p> 会话处理逻辑 </p>
 *
 * @author hufei
 * @date 2022/7/17 20:04
 */
public class SessionShare extends Session {

    public SessionShare(String username, String name, String sex, String role, Integer level) {
        super(username, name, sex, role, level);
    }

    @Override
    public void handler() {
        System.out.println("要共享用户信息了！");
    }
}
