package org.hf.common.publi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * <p> 表达式解析工具类辅助类 </p>
 *  通过@PropertySource注解将配置文件中的值存储到spring的Environment对象中
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/12 10:14
 */
@Configuration()
@PropertySource("classpath:application.properties")
public class ExpressionResolverConfiguration {
}
