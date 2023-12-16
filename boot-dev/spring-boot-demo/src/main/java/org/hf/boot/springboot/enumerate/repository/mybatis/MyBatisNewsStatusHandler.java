package org.hf.boot.springboot.enumerate.repository.mybatis;

import org.apache.ibatis.type.MappedTypes;
import org.hf.boot.springboot.enumerate.NewsStatus;

/**
 * mybatis自定义指定枚举转换处理器
 * 自定义枚举转换及查询处理 - 8
 */
@MappedTypes(NewsStatus.class)
public class MyBatisNewsStatusHandler extends CommonEnumTypeHandler<NewsStatus> {
    public MyBatisNewsStatusHandler() {
        super(NewsStatus.values());
    }
}