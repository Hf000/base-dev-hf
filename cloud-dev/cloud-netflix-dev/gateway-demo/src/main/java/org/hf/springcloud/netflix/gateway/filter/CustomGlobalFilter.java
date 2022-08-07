package org.hf.springcloud.netflix.gateway.filter;

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
 * @Author:hufei
 * @CreateTime:2020-10-28
 * @Description:自定义全局过滤器
 */
@Component      //因为是spring组件，所以需要交给spring容器管理
public class CustomGlobalFilter implements GlobalFilter, Ordered {  //实现Ordered接口是为了实现过滤器的执行顺序排序

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {//全局过滤器拦截方法
        ServerHttpRequest request = exchange.getRequest();//获取请求对象，参数
        String token = request.getQueryParams().getFirst("token");//拦截参数名称为token的参数
        System.out.println("全局过滤器参数token："+token);
        if (StringUtils.isBlank(token)) {//判断是否有请求参数token
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);//设置响应状态码为未授权
            return exchange.getResponse().setComplete();//将此请求设置成完成
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {//实现过滤器执行顺序排序方法，返回值越小越先执行
        return 1;//值越小越先执行
    }
}
