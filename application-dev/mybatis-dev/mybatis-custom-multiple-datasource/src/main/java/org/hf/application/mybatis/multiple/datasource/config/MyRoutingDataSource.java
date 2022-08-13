package org.hf.application.mybatis.multiple.datasource.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.lang.Nullable;

/**
 * <p> 获取数据源路由key </p>
 * 自定义实现多数据源 - 5
 * @author hufei
 * @date 2022/8/13 9:43
*/
public class MyRoutingDataSource extends AbstractRoutingDataSource {
    @Nullable
    @Override
    protected Object determineCurrentLookupKey() {
        return DBContextHolder.get();
    }
}
