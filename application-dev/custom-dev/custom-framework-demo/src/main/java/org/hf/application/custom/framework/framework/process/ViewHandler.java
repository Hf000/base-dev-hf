package org.hf.application.custom.framework.framework.process;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>  </p>
 * 定义不同渲染（响应）方式
 * @author hufei
 * @date 2022/7/17 19:24
*/
public interface ViewHandler {

    //json输出
    default void print(HttpServletResponse response, Object result){};

    //转发
    default void forward(HttpServletRequest request, HttpServletResponse response, Object result){}
}
