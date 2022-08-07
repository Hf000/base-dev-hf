package org.hf.application.javabase.apply.six.old;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class BookDao extends MySQLUtil {

    //查询
    public ResultSet query() throws Exception{
        //获得数据库链接
        Connection conn = super.getConn();
        PreparedStatement ps = conn.prepareStatement("select * from table");
        return ps.executeQuery();
    }
}
