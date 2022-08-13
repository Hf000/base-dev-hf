package org.hf.application.mybatis.multiple.datasource.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * <p> Mybatis配置类 </p>
 * 自定义实现多数据源 - 6
 * //@EnableTransactionManagement   // 开启springboot的事务支持注解，等同于xml配置方式的 <tx:annotation-driven />
 * @author hufei
 * @date 2022/8/13 9:51
*/
@EnableTransactionManagement
@Configuration
public class MybatisConfig {
    @Resource(name = "myRoutingDataSource")
    private DataSource myRoutingDataSource;

    @Value("${mybatis.mapper-locations}")
    private String mapperLocations;

    @Value("${mybatis.type-aliases-package}")
    private String typeAliasesPackage;

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        // 如果需要mybatis-plus的默认方法，则需要使用此sqlSessionFactoryBean
        // MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(myRoutingDataSource);
        //手动设置数据源，需要手动指定mapper文件加载路径和类的别名
        //如果有mapper.xml文件，这里需要设置其路径,  注意：getResource()方法加载指定名称文件， getResources()加载该路径下的所有文件
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
        //配置mpper对应的实体类包路径
        sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasesPackage);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager() {
        return new DataSourceTransactionManager(myRoutingDataSource);
    }
}
