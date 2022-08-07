package org.hf.application.custom.shop.user;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/7/17 20:04
*/
public class SessionShare extends Session {

    //构造函数
    public SessionShare(String username, String name, String sex, String role, Integer level) {
        super(username, name, sex, role, level);
    }

    //额外功能实现
    @Override
    public void handler() {
        System.out.println("要共享用户信息了！");
    }
}
