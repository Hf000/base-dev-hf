package org.hf.springcloud.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p> 自定义局部过滤器，类名必须以GatewayFilterFactory结尾，配置文件中只需要配置除去GatewayFilterFactory的前面部分即可 </p>
 * //@Component //因为是spring组件，所以需要交给spring容器管理
 * @author hufei
 * @date 2022/8/21 17:27
*/
@Component
public class CustomLocalGatewayFilterFactory extends AbstractGatewayFilterFactory<CustomLocalGatewayFilterFactory.Config> {

    /**
     * 和下面自定义配置类中的参数保持一致
     */
    public static final String PARAM_NAME = "param";

    public CustomLocalGatewayFilterFactory() {//参考其他过滤器实现构造方法
        super(Config.class);
    }

    /**
     * 参考其他过滤器实现加载过滤参数方法
     * @return List<String>
     */
    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.singletonList(PARAM_NAME);
    }

    /**
     * 实现父类实现过滤参数拦截的方法
     * @param config 入参
     * @return GatewayFilter
     */
    @Override
    public GatewayFilter apply(Config config) {
        //Lmbda表达式方式实现匿名内部类接口方法
        return (exchange, chain) -> {
            //获取请求对象, 获得请求对象中的请求参数
            ServerHttpRequest request = exchange.getRequest();
            if (request.getQueryParams().containsKey(config.param)) {
                request.getQueryParams().get(config.param).forEach(value -> System.out.printf("局部过滤器中的请求参数：%s = %s", config.param, value));
            }
            return chain.filter(exchange);
        };
    }

    /**
     * 自定义局部过滤器配置类
     */
    public static class Config {
        //对应application.yml中的自定义局部过滤器指定的参数名，这里不需要保持一致，加载的时候会自动注入值
        private String param;
        public String getParam() {
            return param;
        }
        public void setParam(String param) {
            this.param = param;
        }
    }
}
