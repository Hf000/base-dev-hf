package org.hf.springboot.service.config;

import org.apache.ibatis.plugin.Interceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p> 自定义mybatis配置 </p >
 *
 * @author hufei
 * @date 2022-09-05
 **/
@Configuration
@ConditionalOnProperty(name = "mybatis.plugins.enabled", havingValue = "true")
public class MyBatisPluginsConfig {

    /**
     * mybatis plugins拦截器,自动装配
     */
    @Bean
    public Interceptor getInterceptor() {
        return new SqlCostInterceptor();
    }
}