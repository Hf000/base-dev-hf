package org.hf.application.javabase.apply.six.news;

import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class BookDao  {

    @Autowired
    private MySQLUtil mySQLUtil;

    //查询
    public ResultSet query() throws Exception{
        //获得数据库链接
        Connection conn = mySQLUtil.getConn();
        PreparedStatement ps = conn.prepareStatement("select * from table");
        return ps.executeQuery();
    }
}
