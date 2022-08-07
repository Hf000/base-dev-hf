package org.hf.application.javabase.apply.seven.old;


public class UserController {

    private UserDao userDao;
    private LogDao logDao;
    private GoodsDao goodsDao;

    //查询用户信息和用户订单
    public void modifyPassword(){
        userDao.modify();
        logDao.recode();
    }
}
