package org.hf.boot.springboot.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.lang.Nullable;

/**
 * @Author:hufei
 * @CreateTime:2020-11-16
 * @Description:获取数据源路由key
 * 自定义实现多数据源 - 5
 */
public class MyRoutingDataSource extends AbstractRoutingDataSource {
    @Nullable
    @Override
    protected Object determineCurrentLookupKey() {
        return DBContextHolder.get();
    }
}
