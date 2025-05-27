package org.hf.boot.springboot.dynamic.statement;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 动态statement配置
 */
@Configuration
public class DynamicStatementDataApiConfig {

    @Bean(name = "dynamicStatementDataApiManager")
    public DynamicStatementDataApiManager dynamicDataApiManager(@Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping handlerMapping,
                                                                DynamicMappedStatementBuilder mappedStatementBuilder,
                                                                @Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory,
                                                                DynamicStatementConfigService dynamicStatementConfigService
                                                       ) {
        return new DynamicStatementDataApiManager(handlerMapping,
                mappedStatementBuilder,
                sqlSessionFactory,
                dynamicStatementConfigService);
    }
}
