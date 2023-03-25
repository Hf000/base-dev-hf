package org.hf.application.custom.framework.framework.process;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p> 视图渲染接口 </p>
 * 决策使用何种渲染方式
 * @author hufei
 * @date 2022/7/17 19:24
 */
public interface View {

    /**
     * 视图渲染方法
     * @param request   请求对象
     * @param response  响应对象
     * @param result    响应结果
     */
    void render(HttpServletRequest request, HttpServletResponse response, Object result);

}
