package org.hf.boot.springboot.dynamic.datasource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据源配置
 * 注意点1: //@EnableTransactionManagement   //开启springboot的事务支持注解，等同于xml配置方式的 <tx:annotation-driven />
 * 注意点2: //@MapperScan //数据源配置类也可以通过该注解进行指定的数据源扫描配置, 同时指定具体的SqlSessionFactory及事务配置, 这种方式适用
 *  于多个数据源绑定多个SqlSessionFactory时配置, 但这样就需要在DAO层(mapper)中显示的指定具体的SqlSessionFactory来操作不同的数据库, 注意
 *  这里就需要将不同数据源的mapper接口和xml文件都放在不同的扫描路径下, 否则容易出现都扫同一个路径而导致数据源切换失效的问题; 但此示例是通过注解方
 *  式选择数据源, 将配置的多个数据源设置到DynamicDataSource对象上, 然后绑定同一个SqlSessionFactory所以不需要进行此配置
 * 注意点3: //@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})  //在启动类增加排除指定的类,原因是自动加载
 *  数据源会产生冲突,暂时没出现此问题所以在启动类注释掉了
 * 注意点4: 也可以通过这种方式配置多个数据, 然后根据@MapperScan注解扫描指定包下的mapper接口和setMapperLocations()方法扫描指定的xml文件的方式,
 *  来区分不同数据源的数据访问
 * 自定义多数据源处理 - 1
 */
@Configuration
//@EnableTransactionManagement
//@MapperScan(basePackages = "org.hf.boot.**.master.dao", sqlSessionTemplateRef = "sqlSessionFactory")
public class DateSourceConfig {

    /**
     * 通过解析配置文件数据源信息创建数据源
     */
    @Bean
    @ConfigurationProperties("spring.datasource.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.slave")
    public DataSource slaveDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * // @Primary 标记一下如果有多个相同类型的实例,取这个主要实例
     * 数据源动态配置
     */
    @Bean(name = "dynamicDataSource")
    @Primary
    public DynamicDataSource createDynamicDataSource() {
        Map<Object, Object> dataSourceMap = new HashMap<>();
        DataSource defaultDataSource = masterDataSource();
        dataSourceMap.put("master", defaultDataSource);
        dataSourceMap.put("slave", slaveDataSource());
        return new DynamicDataSource(defaultDataSource, dataSourceMap);
    }

    /**
     * 声明式事务需要手动进行数据源事务配置, 需要在此类上标记@EnableTransactionManagement注解, 进行事务开启
     * 因为springboot默认只会为一个数据源(或者说默认数据源)创建一个事务管理器, 如果不手动创建可能出现事务不生效的问题
     * 这里也可以在方法入参中通过@Qualifier注解指定具体的数据源, 这里是获取动态数据源
     * TODO 这里暂时注释掉, 没有进行多个不同地址实例的数据源测试, 只进行了同实例不同库数据源测试, 暂时没有事务问题, 如果遇到多实例需要进行验证测试
     */
    /*@Bean
    public PlatformTransactionManager transactionManager(@Qualifier("masterDataSource") DataSource ds) {
        return new DataSourceTransactionManager(createDynamicDataSource());
    }*/

    /**
     * 手动进行数据源的SqlSessionFactory配置, 动态数据源绑定SqlSessionFactory
     * 这里也可以在方法入参中通过@Qualifier注解指定具体的数据源
     * TODO 这里暂时注释掉, 没有进行多个不同地址实例的数据源测试, 只进行了同实例不同库数据源测试, 暂时没有事务问题, 如果遇到多实例需要进行验证测试
     */
    /*@Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        // 这是SqlSession的数据源
        sqlSessionFactoryBean.setDataSource(createDynamicDataSource());
        // 以下配置也可以通过配置文件设置(这里通过配置文件配置)
        // 设置驼峰命名
//        Objects.requireNonNull(sqlSessionFactoryBean.getObject()).getConfiguration().setMapUnderscoreToCamelCase(true);
        // 如果有mapper.xml文件，这里需要设置其路径,  注意：getResource()方法加载指定名称文件， getResources()加载该路径下的所有文件
//        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
        // 配置mpper对应的实体类包路径
//        sqlSessionFactoryBean.setTypeAliasesPackage("org.hf.boot.springboot.pojo.entity");
        return sqlSessionFactoryBean.getObject();
    }*/

    /**
     * 配置SqlSession模板对象
     */
    /*@Bean
    @Primary
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }*/
}