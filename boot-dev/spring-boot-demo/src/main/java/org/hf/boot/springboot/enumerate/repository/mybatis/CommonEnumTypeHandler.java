package org.hf.boot.springboot.enumerate.repository.mybatis;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.hf.boot.springboot.enumerate.CommonEnum;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * mybatis自定义枚举转换通用处理器
 * 自定义枚举转换及查询处理 - 7
 */
public abstract class CommonEnumTypeHandler<T extends Enum<T> & CommonEnum> extends BaseTypeHandler<T> {
    private final List<T> commonEnums;

    protected CommonEnumTypeHandler(T[] commonEnums){
        this(Arrays.asList(commonEnums));
    }

    protected CommonEnumTypeHandler(List<T> commonEnums) {
        this.commonEnums = commonEnums;
    }

    /**
     * 插入时设置参数类型
     * @param preparedStatement     SQL预编译对象
     * @param i                     需要赋值的索引位置(相当于在JDBC中对占位符的位置进行赋值)
     * @param t                     索引位置i需要赋的值(原本要给这个位置赋的值，在setNonNullParameter方法中主要解决的问题就是将这个自定义类型变成数据库认识的类型)
     * @param jdbcType              jdbc的类型
     */
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, T t, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, t.getCode());
    }

    /**
     * 获取时转换回的自定义类型 - 根据列名获取
     * @param resultSet     结果集
     * @param columnName    列名
     */
    @Override
    public T getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        int code = resultSet.getInt(columnName);
        return commonEnums.stream()
                .filter(commonEnum -> commonEnum.match(String.valueOf(code)))
                .findFirst()
                .orElse(null);
    }

    /**
     * 获取时转换回的自定义类型 - 根据索引位置获取
     * @param resultSet     结果集
     * @param i             索引列
     */
    @Override
    public T getNullableResult(ResultSet resultSet, int i) throws SQLException {
        int code = resultSet.getInt(i);
        return commonEnums.stream()
                .filter(commonEnum -> commonEnum.match(String.valueOf(code)))
                .findFirst()
                .orElse(null);
    }

    /**
     * 获取时转换回的自定义类型 - 根据存储过程获取
     * @param callableStatement     结果集
     * @param i                     索引列
     */
    @Override
    public T getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        int code = callableStatement.getInt(i);
        return commonEnums.stream()
                .filter(commonEnum -> commonEnum.match(String.valueOf(code)))
                .findFirst()
                .orElse(null);
    }
}