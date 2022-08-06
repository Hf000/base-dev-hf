package org.hf.boot.springboot.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author:hufei
 * @CreateTime:2020-09-04
 * @Description:jdbc配置类
 * 自定义实现多数据源 - 3
 */
@Configuration                                            //指定当前类为配置类，采用了默认的数据库连接池所以这个配置类不需要了
//@PropertySource("classpath:jdbc.properties")             //方式一：指定配置文件路径  通过引入配置文件的方式创建数据源
//@EnableConfigurationProperties(JdbcProperties.class)      //方式二：指定配置项类
public class JdbcConfig {

    /*@Value("${jdbc.driverClassName}")                   //从配置文件中获取对应key的值并赋值给当前变量，只能注入基本类型
    private String driverClassName;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;

    @Bean                                               //将当前方法返回的返回的bean对象交给spring管理
    public DataSource getDataSource() {
        DruidDataSource dataSource = new DruidDataSource();         //配置druid数据源
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }*/

    /*@Bean                                               //将当前方法返回的返回的bean对象交给spring管理
    public DataSource getDataSource(JdbcProperties jdbcProperties) {
        DruidDataSource dataSource = new DruidDataSource();         //配置druid数据源
        dataSource.setDriverClassName(jdbcProperties.getDriverClassName());
        dataSource.setUrl(jdbcProperties.getUrl());
        dataSource.setUsername(jdbcProperties.getUsername());
        dataSource.setPassword(jdbcProperties.getPassword());
        return dataSource;
    }*/

    /*@Bean             //因为使用了默认的数据库连接池所以不需要指定数据源了，只需要配置参数即可
    @ConfigurationProperties(prefix = "jdbc")               //方式三：直接将配置项文件中的属性注入到和当前对象名称一致的变量上，注意当前对象和配置项文件中名称一样的成员变量必须要有set方法， 该注解也可以作用在类上(此时则需要通过@EnableConfigurationProperties注解绑定配置类了)
    public DataSource getDataSource() {
        return new DruidDataSource();         //配置druid数据源
    }*/

    /****************************配置多数据源开始*****************************/
    @Bean
    @ConfigurationProperties("spring.datasource.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.slave1")
    public DataSource slave1DataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public DataSource myRoutingDataSource(@Qualifier("masterDataSource") DataSource masterDataSource,
                                          @Qualifier("slave1DataSource") DataSource slave1DataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DBType.MASTER, masterDataSource);
        targetDataSources.put(DBType.SLAVE1, slave1DataSource);
        MyRoutingDataSource myRoutingDataSource = new MyRoutingDataSource();
        myRoutingDataSource.setDefaultTargetDataSource(masterDataSource);//设置默认的数据源
        myRoutingDataSource.setTargetDataSources(targetDataSources);
        return myRoutingDataSource;
    }
    /****************************配置多数据源结束*****************************/

}
