package org.hf.boot.springboot.dynamic.datasource;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.hf.boot.springboot.pojo.dto.DataSourceDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.sql.DriverManager;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 获取数据源路由key
 * 自定义多数据源处理 - 2
 */
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {

    private final Map<Object, Object> targetDataSourceMap;

    public DynamicDataSource(DataSource defaultDataSource, Map<Object, Object> targetDataSources) {
        // 设置默认数据源
        super.setDefaultTargetDataSource(defaultDataSource);
        // 设置目标数据源, 保存所有的数据源信息
        /* 在进行数据源路由时org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource.determineTargetDataSource方法
         * 根据org.hf.boot.springboot.dynamic.datasource.DynamicDataSource.determineCurrentLookupKey获取到的可以去
         * resolvedDataSources对象中获取数据源
         */
        super.setTargetDataSources(targetDataSources);
        /* 这里上面设置完后不需要进行super.afterPropertiesSet()方法调用, 是因为初始化对象时会进行父类中此方法的调用,将目标数据源放入
         * resolvedDataSources对象中
         */
        this.targetDataSourceMap = targetDataSources;
    }

    /**
     * 通过重写此方法实现数据源的切换
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDataSource();
    }

    /**
     * 添加数据库配置的数据源信息,通过数据库存储的数据库连接信息创建数据源
     * @param dataSources 数据源实体集合
     */
    public void createDataSource(List<DataSourceDTO> dataSources) {
        try {
            if (CollectionUtils.isNotEmpty(dataSources)) {
                for (DataSourceDTO ds : dataSources) {
                    //校验数据库是否可以连接, 不可以则抛出异常
                    Class.forName(ds.getDriverClassName());
                    DriverManager.getConnection(ds.getUrl(), ds.getUsername(), ds.getPassword());
                    // 判断数据源的key是否存在
                    if (existsDataSource(ds.getKey())) {
                        log.info("添加的数据源信息重复={}", ds);
                        continue;
                    }
                    //定义数据库
                    HikariDataSource dataSource = new HikariDataSource();
                    // 这里可以通过HikariConfig设置连接属性配置
                    BeanUtils.copyProperties(ds, dataSource);
                    dataSource.setJdbcUrl(ds.getUrl());
                    this.targetDataSourceMap.put(ds.getKey(), dataSource);
                    log.info("------添加数据库配置数据源-----：" + ds.getKey() + ":成功");
                }
                super.setTargetDataSources(this.targetDataSourceMap);
                // 这里需要手动调用改方法, 将目标数据源TargetDataSources中的连接信息放入resolvedDataSources管理
                super.afterPropertiesSet();
            }
        } catch (Exception e) {
            log.error("---添加数据库配置数据源报错---:{}", e.getMessage());
        }
    }

    /**
     * 校验数据源是否存在
     *
     * @param key 数据源保存的key
     * @return 返回结果，true：存在，false：不存在
     */
    public boolean existsDataSource(String key) {
        return Objects.nonNull(this.targetDataSourceMap.get(key));
    }
}