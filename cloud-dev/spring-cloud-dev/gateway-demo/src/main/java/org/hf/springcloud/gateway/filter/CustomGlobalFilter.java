package org.hf.springcloud.gateway.filter;

import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * <p> 自定义全局过滤器 </p>
 * //@Component //因为是spring组件，所以需要交给spring容器管理
 * //implements GlobalFilter, Ordered   //实现Ordered接口是为了实现过滤器的执行顺序排序
 * @author hufei
 * @date 2022/8/21 17:25
*/
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    /**
     * 全局过滤器拦截方法
     * @param exchange 入参
     * @param chain 入参
     * @return Mono<Void>
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取请求对象，参数
        ServerHttpRequest request = exchange.getRequest();
        //拦截参数名称为token的参数
        String token = request.getQueryParams().getFirst("token");
        System.out.println("全局过滤器参数token："+token);
        //判断是否有请求参数token
        if (StringUtils.isBlank(token)) {
            //设置响应状态码为未授权
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            //将此请求设置成完成
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }

    /**
     * 实现过滤器执行顺序排序方法，返回值越小越先执行
     * @return int
     */
    @Override
    public int getOrder() {
        //值越小越先执行
        return 1;
    }
}
