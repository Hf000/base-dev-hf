package org.hf.boot.springboot.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.JdbcProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p> jdbc配置类 </p>
 * 单独使用<artifactId>druid</artifactId>依赖时需要配置此配置类, 如果采用默认的数据库连接池所以这个配置类不需要了
 * 方式一：指定配置文件路径  通过引入配置文件的方式创建数据源
 * //@PropertySource("classpath:jdbc.properties")
 * 方式二：指定配置项类
 * //@EnableConfigurationProperties(JdbcProperties.class)   //需要配合@Configuration使用, 引入org.hf.boot.springboot.config.JdbcProperties类
 * @author hufei
 * @date 2022/8/13 10:55
*/
//@Configuration
public class JdbcConfig {

    /**
     * 从配置文件中获取对应key的值并赋值给当前变量，只能注入基本类型
     */
    /*@Value("${jdbc.driverClassName}")
    private String driverClassName;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;
    /**
     * //@Bean  //将当前方法返回的返回的bean对象交给spring管理
     * @return 数据源对象
     */
    /*@Bean
    public DataSource getDataSource() {
        // 配置druid数据源
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }*/

    /**
     * 初始化数据源对象
     * @param jdbcProperties 数据源参数对象参数
     * @return 数据源对象
     */
    /*@Bean
    public DataSource getDataSource(JdbcProperties jdbcProperties) {
        DruidDataSource dataSource = new DruidDataSource();         //配置druid数据源
        dataSource.setDriverClassName(jdbcProperties.getDriverClassName());
        dataSource.setUrl(jdbcProperties.getUrl());
        dataSource.setUsername(jdbcProperties.getUsername());
        dataSource.setPassword(jdbcProperties.getPassword());
        return dataSource;
    }*/

    /**
     * 方式三：直接将配置项文件中的属性注入到和当前对象名称一致的变量上，注意当前对象和配置项文件中名称一样的成员变量必须要有set方法， 该注解也可以作用在类上(此时则需要通过@EnableConfigurationProperties注解绑定配置类了)
     * @return 数据源对象
     */
    /*@Bean
    @ConfigurationProperties(prefix = "jdbc")
    public DataSource getDataSource() {
        //配置druid数据源
        return new DruidDataSource();
    }*/

}
