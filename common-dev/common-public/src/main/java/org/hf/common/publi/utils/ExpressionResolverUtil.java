package org.hf.common.publi.utils;

import org.apache.commons.lang3.StringUtils;
import org.hf.common.publi.constants.CommonConstant;
import org.hf.common.publi.config.ExpressionResolverConfiguration;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.expression.StandardBeanExpressionResolver;

import static org.hf.common.publi.constants.CommonConstant.AFTER_BIG_BRACKETS;
import static org.hf.common.publi.constants.CommonConstant.BEFORE_BIG_BRACKETS;

/**
 * <p> EL表达式解析工具类 </p>
 *
 * @author hufei
 * @version 1.0.0
 * @date 2021/10/12 9:48
 */
public class ExpressionResolverUtil {

    /**
     * 表达式语言处理器
     */
    protected static final BeanExpressionResolver RESOLVER = new StandardBeanExpressionResolver();

    /**
     * 解析EL表达式获取配置文件中配置的值
     * @param configurableBeanFactory  注入了属性的容器对象
     * @param param 一个固定值或一个${}表达式
     * @return 值
     */
    protected static Object expressionAnalysis(ConfigurableBeanFactory configurableBeanFactory, String param) {
        if (null == configurableBeanFactory || StringUtils.isBlank(param)) {
            return null;
        }
        String resolveEmbeddedValue = configurableBeanFactory.resolveEmbeddedValue(param);
        if (null == resolveEmbeddedValue) {
            return null;
        }
        if (!(StringUtils.startsWith(resolveEmbeddedValue, CommonConstant.DOLLAR_SIGN + BEFORE_BIG_BRACKETS) && StringUtils.endsWith(resolveEmbeddedValue, AFTER_BIG_BRACKETS))) {
            return resolveEmbeddedValue;
        }
        return RESOLVER.evaluate(resolveEmbeddedValue, new BeanExpressionContext(configurableBeanFactory, null));
    }

    /**
     * 已配置属性容器对象
     * @param param 表达式
     * @return 值
     */
    public static Object expressionAnalysis(String param) {
        // 使用ExpressionResolverUtil解析表达式, 需要一个配置类辅助(辅助类需要加载到spring环境中, 加上@Configuration()), 并在配置类上添加@PropertySource("classpath:application.properties"), 其值为配置文件的相对路径
        return expressionAnalysis(new AnnotationConfigApplicationContext(ExpressionResolverConfiguration.class).getBeanFactory(), param);
    }

}
