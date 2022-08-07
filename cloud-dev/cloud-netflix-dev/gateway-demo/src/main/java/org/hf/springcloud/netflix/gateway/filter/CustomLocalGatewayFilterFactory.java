package org.hf.springcloud.netflix.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
*@Author:hufei
*@CreateTime:2020-10-28
*@Description:自定义局部过滤器，类名必须以GatewayFilterFactory结尾，配置文件中只需要配置除去GatewayFilterFactory的前面部分即可
*/
@Component              //因为是spring组件，所以需要交给spring容器管理
public class CustomLocalGatewayFilterFactory extends AbstractGatewayFilterFactory<CustomLocalGatewayFilterFactory.Config> {

    public static final String PARAM_NAME = "param";//和下面自定义配置类中的参数保持一致

    public CustomLocalGatewayFilterFactory() {//参考其他过滤器实现构造方法
        super(Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {//参考其他过滤器实现加载过滤参数方法
        return Arrays.asList(PARAM_NAME);
    }

    @Override
    public GatewayFilter apply(Config config) {//实现父类实现过滤参数拦截的方法
        return (exchange, chain) -> {       //Lmbda表达式方式实现匿名内部类接口方法
            ServerHttpRequest request = exchange.getRequest();//获取请求对象, 获得请求对象中的请求参数
            if (request.getQueryParams().containsKey(config.param)) {
                request.getQueryParams().get(config.param).forEach(value -> System.out.printf("局部过滤器中的请求参数：%s = %s", config.param, value));
            }
            return chain.filter(exchange);
        };
    }

    public static class Config {//自定义局部过滤器配置类
        private String param;//对应application.yml中的自定义局部过滤器指定的参数名，这里不需要保持一致，加载的时候会自动注入值
        public String getParam() {
            return param;
        }
        public void setParam(String param) {
            this.param = param;
        }
    }
}
