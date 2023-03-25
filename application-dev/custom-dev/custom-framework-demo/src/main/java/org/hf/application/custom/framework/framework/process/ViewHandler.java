package org.hf.application.custom.framework.framework.process;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p> 视图渲染处理器 </p>
 * 定义不同渲染（响应）方式
 * @author hufei
 * @date 2022/7/17 19:24
 */
public interface ViewHandler {

    /**
     * json输出
     * @param response 响应对象
     * @param result   响应结果
     */
    default void print(HttpServletResponse response, Object result){
        // 这里可以进行默认实现
    }

    /**
     * 转发
     * @param request 请求对象
     * @param response 响应对象
     * @param result 响应结果
     */
    default void forward(HttpServletRequest request, HttpServletResponse response, Object result) {
        // 这里可以做默认实现
    }
}
