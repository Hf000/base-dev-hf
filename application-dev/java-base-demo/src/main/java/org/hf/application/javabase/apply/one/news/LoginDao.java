package org.hf.application.javabase.apply.one.news;

import org.springframework.beans.factory.annotation.Autowired;


public class LoginDao {

    @Autowired
    private DBUtil dbUtil;

    //查询数据库
    public Object query() {
        return dbUtil.getConn();
    }
}
